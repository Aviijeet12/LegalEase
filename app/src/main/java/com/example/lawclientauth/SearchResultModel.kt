package com.example.lawclientauth

/**
 * Wrapper for heterogeneous search results.
 */
data class SearchResultModel(
    val type: String, // "lawyer" | "case" | "document" | "appointment"
    val title: String,
    val subtitle: String?,
    val extra: Any? // holds the actual model: LawyerModel / CaseModel / DocumentModel / AppointmentModel
) {
    companion object {
        fun lawyer(l: LawyerModel) = SearchResultModel("lawyer", l.name, l.specialization, l)
        fun caseResult(c: CaseModel) = SearchResultModel("case", c.title, c.caseId, c)
        fun document(d: DocumentModel) = SearchResultModel("document", d.name, d.caseId, d)
        fun appointment(a: AppointmentModel) = SearchResultModel("appointment", a.purpose, "${a.date} | ${a.time}", a)
    }
}
