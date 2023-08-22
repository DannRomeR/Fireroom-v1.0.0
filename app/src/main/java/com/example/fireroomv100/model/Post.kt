package com.example.fireroomv100.model

/**
 * @author Daniel Mendoza
 * Model class for Posts and comments
 */
sealed class Entry(
    val text: String,
    val author: String,
    val imageUrl: String?,
    val creationTime: String
) {
    var reactions: List<Reaction>? = null
    var likeCount: Int = reactions?.size?:0


    class Post(text: String, author: String, imageUrl: String?, creationTime: String) :
        Entry(text, author, imageUrl, creationTime) {
        var comments: List<Comment>? = null
        var commentCount: Int = comments?.size?:0
    }

    class Comment(text: String, author: String, imageUrl: String?, creationTime: String) :
        Entry(text, author, imageUrl, creationTime) {

    }
}

/**
 * @author Daniel Mendoza
 * Model class for reactions to posts and comments
 */
sealed class Reaction(val creatorId:String){
    class Like(creatorId:String):Reaction(creatorId)
    class Fun(creatorId:String):Reaction(creatorId)
    class Fury(creatorId:String):Reaction(creatorId)
    class Sadness(creatorId:String):Reaction(creatorId)
    class Love(creatorId:String):Reaction(creatorId)
    class Wow(creatorId:String):Reaction(creatorId)
}


