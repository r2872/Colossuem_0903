package com.example.colossuem_0903.datas

class TopicData(var id: Int, var title: String, var imgURL: String) {

    //    보조 생성자 추가.
    constructor() : this(0, "제목없음", "")

    //    연습. id 값만 받는 보조 생성자
    constructor(id: Int) : this(id, "제목없음", "")
}