package com.app.shotclock.models

class EduSexualOrientationModel {
    var name: String? = null
    var isSelected: Boolean? = false

    constructor()

    constructor(name: String, isSelected: Boolean) {
        this.name = name
        this.isSelected = isSelected
    }
}