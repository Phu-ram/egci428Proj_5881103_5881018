package com.example.project_personaldoctor.Model

class User(var eMail: String,var password: String) {

    constructor():this("","")
    override fun toString(): String {
     return eMail.toString()
   }

}