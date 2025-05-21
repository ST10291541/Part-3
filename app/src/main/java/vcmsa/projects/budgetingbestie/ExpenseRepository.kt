package vcmsa.projects.budgetingbestie

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("expenses")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Insert expense, generating an ID if needed
    suspend fun insertExpense(expense: Expense) {
        val docId = if (expense.id.isEmpty()) {
            collection.document().id
        } else {
            expense.id
        }
        val expenseWithId = expense.copy(id = docId)
        collection.document(docId).set(expenseWithId).await()
    }

    // Get all expenses for a user
    suspend fun getAllExpenses(userId: String): List<Expense> {
        val snapshot = collection.whereEqualTo("userId", userId)
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .await()
        return snapshot.toObjects(Expense::class.java)
    }

    // Get latest three expenses ordered by date DESC for a user
    suspend fun getLatestThreeExpenses(userId: String): List<Expense> {
        val snapshot = collection.whereEqualTo("userId", userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .await()
        return snapshot.toObjects(Expense::class.java)
    }

    // Get expense by ID
    suspend fun getExpenseById(id: String): Expense? {
        val doc = collection.document(id).get().await()
        return if (doc.exists()) doc.toObject(Expense::class.java) else null
    }

    // Update expense (replace document)
    suspend fun updateExpense(expense: Expense) {
        if (expense.id.isNotEmpty()) {
            collection.document(expense.id).set(expense).await()
        } else {
            throw IllegalArgumentException("Expense ID is empty, cannot update")
        }
    }

    // Delete expense
    suspend fun deleteExpense(expense: Expense) {
        if (expense.id.isNotEmpty()) {
            collection.document(expense.id).delete().await()
        } else {
            throw IllegalArgumentException("Expense ID is empty, cannot delete")
        }
    }

    // Calculate total amount for a user
    suspend fun getTotalAmount(userId: String): Double {
        val expenses = collection.whereEqualTo("userId", userId).get().await().toObjects(Expense::class.java)
        return expenses.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
    }

    // Calculate total amount between dates for a user
    suspend fun getTotalAmountBetween(startDate: String, endDate: String, userId: String): Double {
        val expenses = collection.whereEqualTo("userId", userId).get().await().toObjects(Expense::class.java)
        val start = dateFormat.parse(startDate)
        val end = dateFormat.parse(endDate)
        if (start == null || end == null) return 0.0
        val filtered = expenses.filter {
            val expenseDate = dateFormat.parse(it.date)
            expenseDate != null && !expenseDate.before(start) && !expenseDate.after(end)
        }
        return filtered.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
    }

    // Get category totals between dates for a user (Map<Category, TotalAmount>)
    suspend fun getCategoryTotalsBetween(startDate: String, endDate: String, userId: String): Map<String, Double> {
        val expenses = collection.whereEqualTo("userId", userId).get().await().toObjects(Expense::class.java)
        val start = dateFormat.parse(startDate)
        val end = dateFormat.parse(endDate)
        if (start == null || end == null) return emptyMap()

        val filtered = expenses.filter {
            val expenseDate = dateFormat.parse(it.date)
            expenseDate != null && !expenseDate.before(start) && !expenseDate.after(end)
        }

        val categoryTotals = mutableMapOf<String, Double>()
        for (expense in filtered) {
            val amount = expense.amount.toDoubleOrNull() ?: 0.0
            categoryTotals[expense.category] = (categoryTotals[expense.category] ?: 0.0) + amount
        }
        return categoryTotals
    }
}

