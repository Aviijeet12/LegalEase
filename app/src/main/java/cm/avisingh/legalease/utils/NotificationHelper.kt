package cm.avisingh.legalease.utils

import android.content.Context
import cm.avisingh.legalease.services.NotificationService

class NotificationHelper(private val context: Context) {

    private val notificationService = NotificationService(context)
    private val sharedPrefManager = SharedPrefManager(context)

    fun simulateCaseUpdate() {
        if (sharedPrefManager.getUserRole() == "client") {
            notificationService.showCaseUpdateNotification(
                "Property Boundary Dispute",
                "New survey report has been submitted by your lawyer. Please review."
            )
        } else {
            notificationService.showCaseUpdateNotification(
                "State vs Davis",
                "Client has uploaded new evidence documents for review."
            )
        }
    }

    fun simulateHearingReminder() {
        notificationService.showHearingReminder(
            "Miller Property Dispute",
            "Tomorrow at 10:00 AM"
        )
    }

    fun simulateNewMessage() {
        if (sharedPrefManager.getUserRole() == "client") {
            notificationService.showNewMessageNotification(
                "Advocate Sharma",
                "I've reviewed the documents. Let's discuss the strategy."
            )
        } else {
            notificationService.showNewMessageNotification(
                "Robert Miller",
                "I have some questions about the hearing process."
            )
        }
    }
}