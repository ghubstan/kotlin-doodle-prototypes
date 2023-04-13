package io.dongxi.model

interface IAccessoryListContainer {
    fun loadModel(baseProductName: String)
    fun clearModel()
    fun destroy()
}