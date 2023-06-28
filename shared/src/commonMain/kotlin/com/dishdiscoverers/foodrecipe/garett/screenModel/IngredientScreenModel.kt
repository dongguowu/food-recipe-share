//package com.lduboscq.appkickstarter.main.screenModel
//import androidx.compose.runtime.MutableState
//import cafe.adriel.voyager.core.model.StateScreenModel
//import cafe.adriel.voyager.core.model.coroutineScope
//import com.lduboscq.appkickstarter.main.data.IngredientRepositoryRealm
//import com.lduboscq.appkickstarter.model.IngredientData
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.launch
//
//class IngredientScreenModel(private val repository: IngredientRepositoryRealm)
//    : StateScreenModel<IngredientScreenModel.State>(State.Init) {
//
//    private val _ingredientList = MutableStateFlow<List<IngredientData>>(emptyList())
//    val ingredientDatasState: MutableStateFlow<List<IngredientData>> = _ingredientList
//
//    init {
//        coroutineScope.launch {
//            var ingredientDatas: Flow<List<IngredientData>> = repository.getAllIngredientsFlow(null)
//            ingredientDatas.collect {ingredients ->
//                _ingredientList.value = ingredients
//            }
//        }
//    }
//
//    sealed class State {
//        object Init : State()
//        object Loading : State()
//        sealed class Result: State() {
//            class SingleResult(val ingredientData: IngredientData?) : Result()
//            class MultipleResult(val ingredientDatas: MutableState<List<IngredientData>>) : IngredientScreenModel.State.Result()
//            class MultipleResultList(val ingredientDatas: List<IngredientData>?) : Result()
//        }
//    }
//
//    fun getIngredient(ingredientName : String) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//            mutableState.value = State.Result.SingleResult(repository.getIngredient(ingredientName))
//        }
//    }
//
//    fun getAllIngredients(ingredientName: String?) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//            mutableState.value = State.Result.MultipleResult(repository.getAllIngredients(ingredientName))
//        }
//    }
//
//    fun getAllIngredientsList(ingredientName: String?) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//            mutableState.value = State.Result.MultipleResultList(repository.getAllIngredientsList(ingredientName))
//        }
//    }
//
//    /* Sample add function.  It accepts a name string, but fills in the other
//         Ingredient data fields with fixed values for now.
//     */
//
//    fun addIngredient(ingredientName : String, ingredientDetails : String) {
//         coroutineScope.launch {
//            mutableState.value = State.Loading
//            mutableState.value = State.Result.SingleResult(repository.addIngredient(
//                IngredientData(
//                name = ingredientName,
//                details = ingredientDetails,
//                ingredient = null)
//            ))
//        }
//    }
//
//    fun updateIngredient(ingredientName : String, ingredientDetails: String) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//            mutableState.value = State.Result.SingleResult(repository.updateIngredient(ingredientName, ingredientDetails))
//        }
//    }
//
//    fun deleteIngredient(ingredientName : String) {
//        coroutineScope.launch {
//            mutableState.value = State.Loading
//            mutableState.value = State.Result.SingleResult(repository.deleteIngredient(ingredientName))
//        }
//    }
//}
