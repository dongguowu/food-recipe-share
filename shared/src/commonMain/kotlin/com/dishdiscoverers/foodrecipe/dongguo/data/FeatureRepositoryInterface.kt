package com.dishdiscoverers.foodrecipe.dongguo.data

interface FeatureRepositoryInterface {
    fun getFeature(): Message
}

data class Message(val title: String, val content: String, val imageUrl: String?)

class FeatureRepositoryLocal : FeatureRepositoryInterface {
    private val content =
        "Model-View-ViewModel \n" + "AppKickstarter template\n" + "Voyager Screen/ScreenModel\n" + "Mongo Realm Sync database\n" + "Stateless components\n" + "Material design\n" + "Layouts consistency"
    private val url =
        "https://as1.ftcdn.net/v2/jpg/03/16/86/98/1000_F_316869849_oHD2qOXRJlZE2Md6SNv0MtvDcgZyHkfv.jpg"

    override fun getFeature(): Message {
        return Message(title = "feature", content = content, imageUrl = url)
    }
}
