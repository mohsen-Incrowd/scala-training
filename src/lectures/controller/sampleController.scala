package controllers

import controllers.actions.API
import model.dummy.{Todo, TodoWithUser, UserDetails}
import model.{EmailTemplate, EmailTemplateTranslation, User}


class DummyController extends InCrowdController {

  def getTodos:Action[AnyContent] =  API.action { request =>

    val todos : List[JsObject] = Todo.getAllTodos().map(_.json())
    Ok(Json.obj(
      "todos" -> JsArray(todos)
    )) as JSON
  }

  def getaTodo(id:Long):Action[AnyContent] = API.action{request=>
    val todo = Todo.getWithId(id).json()
    Ok(todo) as JSON
  }

  def deleteTask(id: Long): Action[AnyContent] = API.action { request =>
    val todo = Todo.deleteRow(id)
    Ok("TASK IS DELETED SUCCESSFULLY") as JSON
  }

  def addTask(): Action[AnyContent] = API.action { request =>
    request.body.asJson.map(json=>{
      Todo(
        id=0L,
        task= (json \ "task").as[String],
        is_completed = false
      ).create()
    })
    Ok("TASK IS Created Successfully") as JSON
  }

  def updateTask(id:Long): Action[AnyContent] = API.action { request =>

    request.body.asJson.map(json => {
      Todo.updateRow(id,'task->(json \ "task").as[String],
        NamedParameter("is_completed",(json \ "is_completed").as[Boolean]))
    })
    val task = Todo.getWithId(id)
    Ok(task.json()) as JSON
  }

  def getTodosWithUser(): Action[AnyContent] = API.action { request =>
    var tasks = Todo.getAllTodosWtihUsers().map(_.json())
    Ok(Json.obj(
      "todos" -> JsArray(tasks)
    )) as JSON
  }

  def getUserDetails: Action[AnyContent] = API.action{ request =>
    val qs = SimpleQueryParams(request.queryString)
    val email = qs.getString("email") match {
      case Some(x) => x
      case _ => "%"
    }
    val name = qs.getString("name") match {
      case Some(x) => s"%${x}%"
      case _ => "%%"
    }
    val sortDir = qs.sortDir.map(_.toLowerCase) match {
      case Some(x@("asc" | "desc")) => x
      case _ => "desc"
    }

    val limit:Long = qs.limit match {
      case Some(x) => x
      case _ => 10L
    }
    val offset:Long = qs.offset match {
      case Some(x) => x
      case _ => 0L
    }

    if(limit.toInt < 0 || offset.toInt <0){
      throw new Exception("Exception thrown from func")
    }
    val sortBy = qs.sortBy.map(_.toLowerCase) match {
      case Some(x@("id" | "registration_date" | "opted_out" | "opted_out_reason" | "provider_name")) => x
      case _ => "id"
    }
    val optedOut = qs.getString("isOptedOut") match {
      case Some(x) => x
      case None => "%"
    }
    val responderType = qs.getString("responderTypeID") match {
      case Some(x) => x
      case None => "%"
    }

    val users = User.getUserDetails(offset,limit,sortDir,sortBy,optedOut,responderType,email,name).map(_.json())
    Ok(Json.obj(
      "users" -> JsArray(users)
    )) as JSON

  }

  def addCustomEmailLanguages(templateId: Long): Action[AnyContent] = API.secure { (user, request) =>
    if (user.isAnyAdmin) {
      request.body.asJson.map { json => {
//        Validate the json from the request
        json.validate(addCustomEmailLanguagesR) map {
          ids => {
            val emailDetail = EmailTemplate.readWithId(templateId)
            if (emailDetail.isEmpty) {
              notFound("Email template", s"${templateId} not found in the email template table")
            } else {
              ids.map(id =>
                EmailTemplateTranslation.addEmailsLanguage(id, templateId,
                  emailDetail.get.subject, emailDetail.get.htmlText, user.id))
              Ok(Json.obj("message" -> s"languages added for ${templateId} successfully."))
            }
          }
        } recoverTotal (JsonValidationError.process _)
      }
      } getOrElse DoesNotCompute.noJson.httpResult
    } else notAllowedTo("to add languages for the custom email")
  }
}
