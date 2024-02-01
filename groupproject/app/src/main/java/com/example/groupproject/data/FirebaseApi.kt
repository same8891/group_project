package com.example.groupproject.data

import android.net.Uri
import android.util.Log
import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.Feedback
import com.example.groupproject.data.model.Profile
import com.example.groupproject.data.model.Review
import com.example.groupproject.data.model.Trip
import com.example.groupproject.data.model.User
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter.equalTo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class FirebaseApi {

    private val db = FirebaseFirestore.getInstance()

    // Create or update user
    fun saveUser(user: User, userId: String) {
        // Save user data
        db.collection("User")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                //Delete the trip field in the user document
                db.collection("User")
                    .document(userId)
                    .update("trips", FieldValue.delete())
                    .addOnSuccessListener {
                        println("Trips field deleted successfully!")
                    }
                    .addOnFailureListener {
                        println("Error deleting trips field: $it")
                    }
                db.collection("User")
                    .document(userId)
                    .update("profile", FieldValue.delete())
                    .addOnSuccessListener {
                        println("Trips field deleted successfully!")
                    }
                    .addOnFailureListener {
                        println("Error deleting trips field: $it")
                    }
                db.collection("User")
                    .document(userId)
                    .update("reviews", FieldValue.delete())
                    .addOnSuccessListener {
                        println("Trips field deleted successfully!")
                    }
                    .addOnFailureListener {
                        println("Error deleting trips field: $it")
                    }

                println("User saved successfully!")
                // Save Trips subcollection
                user.trips.forEachIndexed { index, trip ->
                    db.collection("User")
                        .document(userId)
                        .collection("trips")
                        .document(trip.tripId)
                        .set(trip)
                        .addOnSuccessListener {
                            println("Trip $index saved successfully!")
                        }
                        .addOnFailureListener {
                            println("Error saving Trip $index: $it")
                        }
                }
                // Remove the trips that are not in the user's trips list
                db.collection("User")
                    .document(userId)
                    .collection("trips")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            val trip = document.toObject(Trip::class.java)
                            if (trip != null && !user.trips.contains(trip)) {
                                document.reference.delete()
                                    .addOnSuccessListener {
                                        println("Trip deleted successfully!")
                                    }
                                    .addOnFailureListener {
                                        println("Error deleting Trip: $it")
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        println("Error getting trips: $it")
                    }
                // Save Profile subcollection
                user.profile.forEachIndexed { index, profile ->
                    db.collection("User")
                        .document(userId)
                        .collection("profile")
                        .document(profile.profileId)
                        .set(profile)
                        .addOnSuccessListener {
                            println("Profile $index saved successfully!")
                        }
                        .addOnFailureListener {
                            println("Error saving Profile $index: $it")
                        }
                }
                // Remove the Profile that are not in the user's Profile list
                db.collection("User")
                    .document(userId)
                    .collection("profile")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            val profile = document.toObject(Profile::class.java)
                            if (profile != null && !user.profile.contains(profile)) {
                                document.reference.delete()
                                    .addOnSuccessListener {
                                        println("Profile deleted successfully!")
                                    }
                                    .addOnFailureListener {
                                        println("Error deleting Profile: $it")
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        println("Error getting Profile: $it")
                    }

                // Save Reviews subcollection
                val newReviewIds = user.reviews.map { it.reviewId }
                user.reviews.forEachIndexed { index, review ->
                    val reviewDocRef = db.collection("User")
                        .document(userId)
                        .collection("reviews")
                        .document(review.reviewId)
                    if (review.reviewId !in newReviewIds) {
                        // Delete the review if it's not in the new list
                        reviewDocRef.delete()
                            .addOnSuccessListener {
                                println("Review $index deleted successfully!")
                            }
                            .addOnFailureListener {
                                println("Error deleting Review $index: $it")
                            }
                    } else {
                        // Save or update the review
                        reviewDocRef.set(review)
                            .addOnSuccessListener {
                                println("Review $index saved successfully!")
                            }
                            .addOnFailureListener {
                                println("Error saving Review $index: $it")
                            }
                    }
                }
                // Remove the reviews that are not in the user's reviews list
                db.collection("User")
                    .document(userId)
                    .collection("reviews")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            val review = document.toObject(Review::class.java)
                            if (review != null && !user.reviews.contains(review)) {
                                document.reference.delete()
                                    .addOnSuccessListener {
                                        println("Review deleted successfully!")
                                    }
                                    .addOnFailureListener {
                                        println("Error deleting review: $it")
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        println("Error getting reviews: $it")
                    }
            }
            .addOnFailureListener {
                println("Error saving user: $it")
            }
    }

    fun getUserByEmail(email: String, callback: (User?) -> Unit) {
        db.collection("User")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDocument = querySnapshot.documents[0]
                    val user = userDocument.toObject(User::class.java)
                    if (user != null) {
                        // Fetch Trips subcollection
                        userDocument.reference.collection("trips")
                            .get()
                            .addOnSuccessListener { tripsSnapshot ->
                                val tripsList = tripsSnapshot.toObjects(Trip::class.java)
                                user.trips = tripsList
                                user.trips.forEachIndexed { index, trip ->
                                    trip.tripId = tripsSnapshot.documents[index].id
                                }
                                // Fetch Profile subcollection
                                userDocument.reference.collection("profile")
                                    .get()
                                    .addOnSuccessListener { profilesSnapshot ->
                                        val profilesList = profilesSnapshot.toObjects(Profile::class.java)
                                        user.profile = profilesList
                                        user.profile.forEachIndexed { index, profile ->
                                            profile.profileId = profilesSnapshot.documents[index].id
                                        }
                                        // Fetch Reviews subcollection
                                        userDocument.reference.collection("reviews")
                                            .get()
                                            .addOnSuccessListener { reviewsSnapshot ->
                                                val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                                                user.reviews = reviewsList
                                                user.reviews.forEachIndexed { index, review ->
                                                    review.reviewId = reviewsSnapshot.documents[index].id
                                                }
                                                callback(user)
                                            }
                                            .addOnFailureListener {
                                                println("Error getting Reviews subcollection: $it")
                                                callback(null)
                                            }
                                    }
                                    .addOnFailureListener {
                                        println("Error getting Profile subcollection: $it")
                                        callback(null)
                                    }
                            }
                            .addOnFailureListener {
                                println("Error getting Trips subcollection: $it")
                                callback(null)
                            }
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting user by email: $it")
                callback(null)
            }
    }


    // Read user
    fun getUser(userId: String, callback: (User?) -> Unit) {
        val userRef = db.collection("User").document(userId)
        userRef.get()
            .addOnSuccessListener { userDocument ->
                if (userDocument.exists()) {
                    val user = userDocument.toObject(User::class.java)

                    // Fetch Trips subcollection
                    userRef.collection("trips")
                        .get()
                        .addOnSuccessListener { tripsSnapshot ->
                            val tripsList = tripsSnapshot.toObjects(Trip::class.java)
                            user?.trips = tripsList
                            user?.userId = userDocument.id
                            // Fetch Profile subcollection
                            userRef.collection("profile")
                                .get()
                                .addOnSuccessListener { profilesSnapshot ->
                                    val profilesList = profilesSnapshot.toObjects(Profile::class.java)
                                    user?.profile = profilesList

                                    // Fetch Reviews subcollection
                                    userRef.collection("reviews")
                                        .get()
                                        .addOnSuccessListener { reviewsSnapshot ->
                                            val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                                            user?.reviews = reviewsList
                                            callback(user)
                                        }
                                        .addOnFailureListener {
                                            println("Error getting Reviews subcollection: $it")
                                            callback(null)
                                        }
                                }
                                .addOnFailureListener {
                                    println("Error getting Profile subcollection: $it")
                                    callback(null)
                                }
                        }
                        .addOnFailureListener {
                            println("Error getting Trips subcollection: $it")
                            callback(null)
                        }
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting user: $it")
                callback(null)
            }
    }

    // Delete user
    fun deleteUser(userId: String) {
        db.collection("User")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                println("User deleted successfully!")
            }
            .addOnFailureListener {
                println("Error deleting user: $it")
            }
    }


    // Get destinations by name
    fun getDestinationByName(destinationName: String, callback: (Destination?) -> Unit) {
        db.collection("Destination")
            .whereEqualTo("name", destinationName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val destination = querySnapshot.documents[0].toObject(Destination::class.java)
                    // Fetch Reviews subcollection
                    if (destination != null) {
                        val destinationId = querySnapshot.documents[0].id
                        val reviewsRef = db.collection("Destination").document(destinationId)
                            .collection("reviews")
                        reviewsRef.get()
                            .addOnSuccessListener { reviewsSnapshot ->
                                val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                                destination.reviews = reviewsList

                                callback(destination)
                            }
                            .addOnFailureListener {
                                println("Error getting Reviews subcollection: $it")
                                callback(null)
                            }
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting destination by name: $it")
                callback(null)
            }
    }


    // Create or update destination
    fun saveDestination(destination: Destination, destinationId: String) {
        // Save destination data
        db.collection("Destination")
            .document(destinationId)
            .set(destination)
            .addOnSuccessListener {
                println("Destination saved successfully!")
                // Save Reviews subcollection
                destination.reviews.forEachIndexed { index, review ->
                    db.collection("Destination")
                        .document(destinationId)
                        .collection("reviews")
                        .document("review_$index")
                        .set(review)
                        .addOnSuccessListener {
                            println("Review $index saved successfully!")
                        }
                        .addOnFailureListener {
                            println("Error saving Review $index: $it")
                        }
                }
            }
            .addOnFailureListener {
                println("Error saving destination: $it")
            }
    }

    // Read destination
    fun getDestination(destinationId: String, callback: (Destination?) -> Unit) {
        val destinationRef = db.collection("Destination").document(destinationId)
        destinationRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val destination = documentSnapshot.toObject(Destination::class.java)

                    // Fetch Reviews subcollection
                    destinationRef.collection("reviews")
                        .get()
                        .addOnSuccessListener { reviewsSnapshot ->
                            val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                            destination?.reviews = reviewsList
                            destination?.destinationId = documentSnapshot.id
                            callback(destination)
                        }
                        .addOnFailureListener {
                            println("Error getting Reviews subcollection: $it")
                            callback(null)
                        }
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting destination: $it")
                callback(null)
            }
    }

    // Delete destination
    fun deleteDestination(destinationId: String) {
        db.collection("Destination")
            .document(destinationId)
            .delete()
            .addOnSuccessListener {
                println("Destination deleted successfully!")
            }
            .addOnFailureListener {
                println("Error deleting destination: $it")
            }
    }

    // Get all destinations
    fun getAllDestinations(callback: (List<Destination>) -> Unit) {
        db.collection("Destination")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val destinationList = mutableListOf<Destination>()
                for (document in querySnapshot) {
                    val destination = document.toObject(Destination::class.java)

                    // Fetch Reviews subcollection for each destination
                    document.reference.collection("reviews")
                        .get()
                        .addOnSuccessListener { reviewsSnapshot ->
                            val reviewsList = reviewsSnapshot.toObjects(Review::class.java)
                            destination.reviews = reviewsList
                            destination.destinationId = document.id
                            destinationList.add(destination)

                            // Check if it's the last document, then call the callback
                            if (destinationList.size == querySnapshot.documents.size) {
                                callback(destinationList)
                            }
                        }
                        .addOnFailureListener {
                            println("Error getting Reviews subcollection: $it")
                            callback(emptyList())
                        }
                }
            }
            .addOnFailureListener {
                println("Error getting all destinations: $it")
                callback(emptyList())
            }
    }

    // Create or update feedback
    fun addFeedback(feedback: Feedback) {
        // 保存反馈数据
        db.collection("Feedback")
            .add(feedback)
            .addOnSuccessListener {
                println("反馈保存成功")
            }
            .addOnFailureListener {
                println("反馈保存失败: $it")
            }
    }


    //Get all feedbacks
    fun getAllFeedbacks(callback: (List<Feedback>) -> Unit) {
        db.collection("Feedback")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val feedbackList = mutableListOf<Feedback>()
                for (document in querySnapshot) {
                    val feedback = document.toObject(Feedback::class.java)
                    // 将文档的ID赋值给feedbackId
                    feedback.feedbackId = document.id
                    feedbackList.add(feedback)
                }
                callback(feedbackList)
            }
            .addOnFailureListener {
                println("Error getting all feedbacks: $it")
                callback(emptyList())
            }
    }



    // Read feedback
    fun getFeedback(feedbackId: String, callback: (Feedback?) -> Unit) {
        db.collection("Feedback")
            .document(feedbackId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val feedback = documentSnapshot.toObject(Feedback::class.java)
                    callback(feedback)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("Error getting feedback: $it")
                callback(null)
            }
    }

    // Delete feedback
    fun deleteFeedback(feedbackId: String) {
        db.collection("Feedback")
            .document(feedbackId)
            .delete()
            .addOnSuccessListener {
                println("Feedback deleted successfully!")
            }
            .addOnFailureListener {
                println("Error deleting feedback: $it")
            }
    }

    fun updateReviewLikes(destination: Destination, userId: String, likes: Int) {
        // 使用 Firestore 查询找到具有特定 userId 的评论
        db.collection("Destination")
            .document(destination.name)
            .collection("reviews")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // 检查是否存在匹配的评论
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    val reviewRef = documentSnapshot.reference

                    // 更新点赞数
                    reviewRef.update("likes", likes)
                        .addOnSuccessListener {
                            // 更新成功
                            println("点赞更新成功")
                        }
                        .addOnFailureListener { e ->
                            // 更新失败
                            println("点赞更新失败: $e")
                        }
                } else {
                    // 没有找到匹配的评论
                    println("找不到匹配的评论")
                }
            }
            .addOnFailureListener { e ->
                // 查询失败
                println("查询评论失败: $e")
            }
    }

    fun updateReview(review: Review, destinationId: String) {
        // 使用 Firestore 查询找到具有特定 userId 的评论
        db.collection("Destination")
            .document(destinationId)
            .collection("reviews")
            .whereEqualTo("userId", review.userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // 检查是否存在匹配的评论
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    val reviewRef = documentSnapshot.reference
                    // 更新评论
                    reviewRef.set(review)
                        .addOnSuccessListener {
                            // 更新成功
                            println("评论更新成功")
                        }
                        .addOnFailureListener { e ->
                            // 更新失败
                            println("评论更新失败: $e")
                        }
                } else {
                    // 没有找到匹配的评论
                    println("找不到匹配的评论")
                }
            }
            .addOnFailureListener { e ->
                // 查询失败
                println("查询评论失败: $e")
            }
    }


    fun addReview(review: Review, destinationId: String) {
        // 保存评论数据
        db.collection("Destination")
            .document(destinationId)
            .collection("reviews")
            .document(review.reviewId + destinationId)
            .set(review)
            .addOnSuccessListener {
                println("评论保存成功")
            }
            .addOnFailureListener {
                println("评论保存失败: $it")
            }
        //add user review
        db.collection("User")
            .document(review.userId)
            .collection("reviews")
            .document(review.reviewId + destinationId)
            .set(review)
            .addOnSuccessListener {
                println("评论保存成功")
            }
            .addOnFailureListener {
                println("评论保存失败: $it")
            }
    }

    fun deleteReview(reviewId: String, destinationId: String) {
        // 删除评论
        db.collection("Destination")
            .document(destinationId)
            .collection("reviews")
            .document(reviewId)
            .delete()
            .addOnSuccessListener {
                println("评论删除成功")
            }
            .addOnFailureListener {
                println("评论删除失败: $it")
            }
    }

    fun addUserSaves(userId: String, destinationId: String) {
        // 获取用户的收藏列表
        db.collection("User")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                if (user != null) {
                    val saves = user.saves.toMutableList()
                    saves.add(destinationId)
                    // 更新用户的收藏列表
                    db.collection("User")
                        .document(userId)
                        .update("saves", saves)
                        .addOnSuccessListener {
                            println("用户收藏更新成功")
                        }
                        .addOnFailureListener {
                            println("用户收藏更新失败: $it")
                        }
                }
            }
            .addOnFailureListener {
                println("获取用户收藏失败: $it")
            }
    }


    fun removeUserSaves(userId: String, destinationId: String) {
        // 获取用户的收藏列表
        db.collection("User")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                if (user != null) {
                    val saves = user.saves.toMutableList()
                    saves.remove(destinationId)
                    // 更新用户的收藏列表
                    db.collection("User")
                        .document(userId)
                        .update("saves", saves)
                        .addOnSuccessListener {
                            println("用户收藏更新成功")
                        }
                        .addOnFailureListener {
                            println("用户收藏更新失败: $it")
                        }
                }
            }
            .addOnFailureListener {
                println("获取用户收藏失败: $it")
            }
    }

    fun updateUserCurrency(userId: String, currency: String) {
        // 更新用户的货币
        db.collection("User")
            .document(userId)
            .update("currency", currency)
            .addOnSuccessListener {
                println("用户货币更新成功")
            }
            .addOnFailureListener {
                println("用户货币更新失败: $it")
            }
    }

    fun saveProfile(userId: String, newProfile: Profile) {
        // 获取用户文档的引用
        val userProfiles = db.collection("User").document(userId).collection("profile")
        // 获取用户的所有个人资料
        userProfiles.get()
            .addOnSuccessListener { querySnapshot ->
                val profiles = querySnapshot.toObjects(Profile::class.java)
                // 如果用户没有个人资料，则直接保存新的个人资料
                if (profiles.isEmpty()) {
                    userProfiles.add(newProfile)
                        .addOnSuccessListener {
                            println("个人资料保存成功")
                        }
                        .addOnFailureListener {
                            println("个人资料保存失败: $it")
                        }
                } else {
                    // 如果用户已经有个人资料，则更新第一个个人资料
                    val profileRef = userProfiles.document(querySnapshot.documents[0].id)
                    profileRef.set(newProfile)
                        .addOnSuccessListener {
                            println("个人资料更新成功")
                        }
                        .addOnFailureListener {
                            println("个人资料更新失败: $it")
                        }
                }
            }
            .addOnFailureListener {
                println("获取个人资料失败: $it")
            }
    }

    fun addTrip(userId: String, newTrip: Trip) {
        // 获取用户文档的引用
        val userTrips = db.collection("User").document(userId).collection("trips")

        // 使用自定义文档ID保存新的旅行
        userTrips.document(newTrip.tripId).set(newTrip)
            .addOnSuccessListener {
                println("旅行保存成功")
            }
            .addOnFailureListener { e ->
                println("旅行保存失败: $e")
            }
    }

    fun deleteTrip(userId: String, tripId: String) {
        // 获取用户文档的引用
        val userTrips = db.collection("User").document(userId).collection("trips")
        // 删除旅行
        userTrips.document(tripId)
            .delete()
            .addOnSuccessListener {
                println("旅行删除成功")
            }
            .addOnFailureListener {
                println("旅行删除失败: $it")
            }
    }

    fun updateTrip(userId: String, tripId: String, newTrip: Trip, callback: (Trip?) -> Unit) {
        // 获取用户文档的引用
        val userTrips = db.collection("User").document(userId).collection("trips")
        // 更新旅行
        userTrips.document(tripId)
            .set(newTrip, SetOptions.merge())
            .addOnSuccessListener {
                println("旅行更新成功")
                callback(newTrip)
            }
            .addOnFailureListener {
                println("旅行更新失败: $it")
                callback(null)
            }
    }

    fun changeUserPassword(userId: String, newPassword: String) {
        // 更新用户的密码
        db.collection("User")
            .document(userId)
            .update("password", newPassword)
            .addOnSuccessListener {
                println("用户密码更新成功")
            }
            .addOnFailureListener {
                println("用户密码更新失败: $it")
            }
    }

    fun changeUserEmail(userId: String, newEmail: String) {
        // 更新用户的邮箱
        db.collection("User")
            .document(userId)
            .update("email", newEmail)
            .addOnSuccessListener {
                println("用户邮箱更新成功")
            }
            .addOnFailureListener {
                println("用户邮箱更新失败: $it")
            }
    }

    fun changeUserDisplayName(userId: String, newDisplayName: String) {
        // 更新用户的显示名称
        db.collection("User")
            .document(userId)
            .update("displayName", newDisplayName)
            .addOnSuccessListener {
                println("用户显示名称更新成功")
            }
            .addOnFailureListener {
                println("用户显示名称更新失败: $it")
            }
    }


    fun uploadImage(userId: String, uri: Uri, callback: (String?) -> Unit) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val imagesRef: StorageReference = storageRef.child("images")
        val fileUri = uri
        val imageName = Uri.parse(fileUri.toString()).lastPathSegment
            ?: ("image" + userId + System.currentTimeMillis() + ".jpg")
        val fileRef = imagesRef.child(userId + System.currentTimeMillis() + imageName )

        fileRef.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                // 文件上传成功，获取下载URL
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    // 将下载URL通过回调函数传递给调用者
                    callback(downloadUrl)
                }
                    .addOnFailureListener { e ->
                        // 获取下载URL失败
                        println("获取下载URL失败: $e")
                        callback(null) // 传递 null 表示失败
                    }
            }
            .addOnFailureListener { e ->
                // 文件上传失败
                println("文件上传失败: $e")
                callback(null) // 传递 null 表示失败
            }
    }

    fun addDestinationToTrip(destination: Destination, user: User, trip: Trip) {
        // 获取用户文档的引用
        val userTrips = db.collection("User").document(user.userId).collection("trips")
        // 获取旅行文档的引用
        val tripRef = userTrips.document(trip.tripId)
        // 获取旅行的目的地列表
        val destinations = trip.destinationList.toMutableList()
        destinations.add(destination.name)
        // 更新旅行的目的地列表
        tripRef.update("destinationList", destinations)
            .addOnSuccessListener {
                println("目的地添加成功")
            }
            .addOnFailureListener {
                println("目的地添加失败: $it")
            }
    }

    fun toggleLike(destination: Destination) {
        // 获取目的地文档的引用
        val destinationRef = db.collection("Destination").document(destination.name)
        // 更新目的地的点赞数
        destinationRef.update("likes", FieldValue.increment(1))
            .addOnSuccessListener {
                println("点赞成功")
            }
            .addOnFailureListener {
                println("点赞失败: $it")
            }
    }

    fun saveUserSaves(destination: Destination, user: User) {
        // 获取用户文档的引用
        val userRef = db.collection("User").document(user.userId)
        // 获取用户的收藏列表
        val saves = user.saves.toMutableList()
        saves.add(destination.name)
        // 更新用户的收藏列表
        userRef.update("saves", saves)
            .addOnSuccessListener {
                println("收藏成功")
            }
            .addOnFailureListener {
                println("收藏失败: $it")
            }
    }

    fun removeDestinationFromSaved(destination: Destination, user: User) {
        // 获取用户文档的引用
        val userRef = db.collection("User").document(user.userId)
        // 获取用户的收藏列表
        val saves = user.saves.toMutableList()
        saves.remove(destination.name)
        // 更新用户的收藏列表
        userRef.update("saves", saves)
            .addOnSuccessListener {
                println("取消收藏成功")
            }
            .addOnFailureListener {
                println("取消收藏失败: $it")
            }
    }

    fun updateProfileImage(userId: String, imageUrl: String) {
        // 更新用户的头像
        db.collection("User")
            .document(userId)
            .collection("profile")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val profileRef = querySnapshot.documents[0].reference
                    profileRef.update("photoImage", imageUrl)
                        .addOnSuccessListener {
                            println("头像更新成功")
                        }
                        .addOnFailureListener {
                            println("头像更新失败: $it")
                        }
                }
            }
    }

    fun addUserProfileImageList(userId: String, imageUrl: String) {
        // 获取用户文档的引用
        val profileRef = db.collection("User").document(userId).collection("profile")
        // 获取用户的头像列表
        profileRef.get()
            .addOnSuccessListener { querySnapshot ->
                val profile = querySnapshot.toObjects(Profile::class.java)
                val uploadImages = profile[0].uploadImages
                uploadImages.add(imageUrl)
                // 更新用户的头像列表
                profileRef.document(querySnapshot.documents[0].id)
                    .update("uploadImages", uploadImages)
                    .addOnSuccessListener {
                        println("图片列表更新成功")
                    }
                    .addOnFailureListener {
                        println("图片列表更新失败: $it")
                    }
            }
            .addOnFailureListener {
                println("获取图片列表失败: $it")
            }
    }

    fun deleteUserReview(userId: String, reviewId: String) {
        // 获取用户文档的引用
        val userRef = db.collection("User").document(userId)
        // 获取用户的评论列表
        userRef.collection("reviews")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val reviews = querySnapshot.toObjects(Review::class.java)
                val newReviews = reviews.filter { it.reviewId != reviewId }
                // 更新用户的评论列表
                userRef.collection("reviews")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            if (document.id == reviewId) {
                                document.reference.delete()
                                    .addOnSuccessListener {
                                        println("评论删除成功")
                                    }
                                    .addOnFailureListener {
                                        println("评论删除失败: $it")
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        println("获取评论列表失败: $it")
                    }
            }
            .addOnFailureListener {
                println("获取评论列表失败: $it")
            }
    }

    fun deleteDestinationReview(destinationId: String, reviewId: String) {
        // 获取目的地文档的引用
        val destinationRef = db.collection("Destination").document(destinationId)
        // 获取目的地的评论列表
        destinationRef.collection("reviews")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    if (document.id == reviewId) {
                        document.reference.delete()
                            .addOnSuccessListener {
                                println("评论删除成功")
                            }
                            .addOnFailureListener {
                                println("评论删除失败: $it")
                            }
                    }
                }
            }
            .addOnFailureListener {
                println("获取评论列表失败: $it")
            }
    }

    fun getUserSavedDestinations(userId: String, callback: (List<Destination>) -> Unit) {
        println("获取${userId}的收藏列表")
        // 获取用户文档的引用
        val userRef = db.collection("User").document(userId)

        // 获取全部的Destination的name
        val destinationNameList = mutableListOf<String>()
        db.collection("Destination")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val destination = document.toObject(Destination::class.java)
                    destinationNameList.add(destination.name)
                }
                println("获取全部的Destination的name: $destinationNameList")



                // 获取用户的收藏列表
                println("获取用户的收藏列表")
                val saves = mutableListOf<String>()
                userRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        val user = documentSnapshot.toObject(User::class.java)
                        if (user != null) {
                            saves.addAll(user.saves)
                            println("获取用户的收藏列表: $saves")
                        }




                        // 筛选出用户收藏的Destination与全部的Destination的name匹配的Destination
                        saves.forEach() {
                            if (!destinationNameList.contains(it)) {
                                saves.remove(it)
                            }
                        }
                        // 获取用户收藏的Destination
                        println("获取用户收藏的Destination: $saves")
                        val savedDestinations = mutableListOf<Destination>()
                        saves.forEach() {
                            db.collection("Destination")
                                .whereEqualTo("name", it)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    if (!querySnapshot.isEmpty) {
                                        println("获取")
                                        val destination = querySnapshot.documents[0].toObject(Destination::class.java)
                                        if (destination != null) {
                                            savedDestinations.add(destination)
                                            callback(savedDestinations)
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    println("获取用户收藏的Destination失败: $it")
                                }
                        }
                    }
                    .addOnFailureListener {
                        println("获取用户收藏失败: $it")
                    }
            }
            .addOnFailureListener {
                println("获取Destination失败: $it")
            }
    }

    fun updateDestinationReview(destination: String, reviewId: String, review: Review) {
        // 获取目的地文档的引用
        val destinationRef = db.collection("Destination").document(destination)
        // 获取目的地的评论列表
        destinationRef.collection("reviews")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    if (document.id == reviewId) {
                        document.reference.set(review)
                            .addOnSuccessListener {
                                println("评论更新成功")
                            }
                            .addOnFailureListener {
                                println("评论更新失败: $it")
                            }
                    }
                }
            }
            .addOnFailureListener {
                println("获取评论列表失败: $it")
            }
    }

    fun getAllDestinationIds(callback: (List<String>) -> Unit) {
        db.collection("Destination")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val destinationIds = mutableListOf<String>()
                for (document in querySnapshot) {
                    destinationIds.add(document.id)
                }
                callback(destinationIds)
            }
            .addOnFailureListener {
                println("获取所有Destination的ID失败: $it")
                callback(emptyList())
            }
    }

    fun getUserTrips(email: String, callback: (List<Trip>?) -> Unit) {
        db.collection("User")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDocument = querySnapshot.documents[0]
                    val userRef = userDocument.reference
                    userRef.collection("trips")
                        .get()
                        .addOnSuccessListener { tripsSnapshot ->
                            val tripsList = tripsSnapshot.toObjects(Trip::class.java)
                            callback(tripsList)
                        }
                        .addOnFailureListener {
                            println("获取用户的旅行失败: $it")
                            callback(null)
                        }
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                println("获取用户失败: $it")
                callback(null)
            }
    }

    fun getTrip(userId: String, tripId: String, callback: (Trip?) -> Unit) {
        db.collection("User")
            .document(userId)
            .collection("trips")
            .document(tripId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val trip = documentSnapshot.toObject(Trip::class.java)
                callback(trip)
            }
            .addOnFailureListener {
                println("获取旅行失败: $it")
                callback(null)
            }
    }
}