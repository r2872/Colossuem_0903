package com.example.colossuem_0903.datas

class SideData(
    var id: Int,
    var topicId: Int,
    var title: String,
    var voteCount: Int
) {
    constructor() : this(0, 0, "", 0)
}