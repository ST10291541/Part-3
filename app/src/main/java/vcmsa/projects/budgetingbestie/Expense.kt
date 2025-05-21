package vcmsa.projects.budgetingbestie

data class Expense(
    val id: String = "",
    var category: String = "",
    var description: String = "",
    var date: String = "",
    var amount: String = "",
    var receiptPhotoUri: String = "",
    var userId: String = ""
)
