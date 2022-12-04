package model.dummy

import anorm.{Macro, NamedParameter, RowParser, SQL, SqlStringInterpolation}
import anorm.SqlParser._
import helper.DataSources
import model.{Answer, CountryLanguageAssociation, CountryLanguageModel, CreateAgent, DeleteAgent, ModelAgent, ModelCompanion, UpdateAgent}
import play.api.libs.json.{JsObject, Json}

case class Todo (id:Long,task:String,is_completed:Boolean) extends  ModelAgent with DeleteAgent with CreateAgent {
  type AgentT = Todo
  override val companion = Todo
  override lazy val params: List[NamedParameter] = List(
    'id -> id,
    'task -> task,
    'is_completed -> is_completed
  )
//  To convert the returned query to json object
  def json ():JsObject={
    Json.obj(
      "id" -> id,
      "task" -> task,
      "is_completed" -> is_completed
    )
  }
}


object Todo extends ModelCompanion[Todo] {
  val tableName = "todos"

  val parser = for {
    id <- get[Long]("id")
    task <- get[String]("task")
    is_completed <- get[Boolean]("is_completed")
  } yield Todo(
    id = id,
    task = task,
    is_completed = is_completed
  )

  def getAllTodos(): List[Todo] = DataSources.defaultDB.withConnection { implicit c =>
    val sql =
      s"""
      select * from todos;
    """
/* 
(SQL(sql).as(Todo.parser *)) 
or               
*/ 
    (SQL(sql).as((for {
      id <- long("id")
      task <- str("task")
      is_completed <- bool("is_completed")
    } yield Todo(id, task, is_completed)) *))
    
  }

  def getAllTodosWtihUsers(): List[TodoWithUser] = DataSources.defaultDB.withConnection { implicit c =>
    val sql =
      s"""
      select todos.*,user.first_name,user.last_name from todos
      inner join ic_user as user on user.id = todos.user_id;
    """

    (SQL(sql).as((for {
      id <- long("id")
      task <- str("task")
      is_completed <- bool("is_completed")
      first_name <- str("first_name")
      last_name <- str("last_name")
    } yield TodoWithUser(id, task, is_completed, first_name, last_name)) *))
  }
}





