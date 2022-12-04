package lectures.packages

object readAndwriteFromJsonRequest {
  def addCustomEmailLanguagesR: Reads[List[Long]] = (__ \ "languageIds").read[List[Long]]

  case class CustomEmailCountryLanguage(templateId: Long, languageId: Long, countryId: Long)

  def addCustomEmailCountryLanguageR(templateId: Long): Reads[CustomEmailCountryLanguage] = ((__ \ "languageId").read[Long] and
    (__ \ "countryId").read[Long]) ((languageId, countryId) => {
    CustomEmailCountryLanguage(templateId, languageId, countryId)
  })
}
