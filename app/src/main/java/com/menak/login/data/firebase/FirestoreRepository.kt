package com.menak.login.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.menak.login.data.Entity.*
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val authRepo: FirebaseAuthRepository
) {

    private val db = FirebaseFirestore.getInstance()

    private fun userId(): String =
        authRepo.currentUserId() ?: throw Exception("User not logged in")


    suspend fun createUserProfile(username: String) {
        val data = mapOf(
            "username" to username
        )

        db.collection("users")
            .document(userId())
            .set(data)
            .await()
    }

    suspend fun addCategory(category: CategoryEntity) {
        db.collection("users")
            .document(userId())
            .collection("categories")
            .add(category)
            .await()
    }

    suspend fun getCategories(): List<CategoryEntity> {
        return db.collection("users")
            .document(userId())
            .collection("categories")
            .get()
            .await()
            .toObjects(CategoryEntity::class.java)
    }


    suspend fun addExpense(expense: ExpenseEntity) {
        db.collection("users")
            .document(userId())
            .collection("expenses")
            .add(expense)
            .await()
    }

    suspend fun getExpenses(): List<ExpenseEntity> {
        return db.collection("users")
            .document(userId())
            .collection("expenses")
            .get()
            .await()
            .toObjects(ExpenseEntity::class.java)
    }

    suspend fun saveBudget(goal: BudgetGoalEntity) {
        db.collection("users")
            .document(userId())
            .collection("budget")
            .document("main")
            .set(goal)
            .await()
    }

    suspend fun saveCategoryLimit(limit: CategoryBudgetLimitEntity) {
        db.collection("users")
            .document(userId())
            .collection("category_limits")
            .add(limit)
            .await()
    }
}