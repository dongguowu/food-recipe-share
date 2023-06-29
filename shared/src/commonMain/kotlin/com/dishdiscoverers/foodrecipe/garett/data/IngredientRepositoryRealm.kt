//package com.lduboscq.appkickstarter.main.data
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import com.dishdiscoverers.foodrecipe.garett.model.IngredientData
//import com.lduboscq.appkickstarter.mains.data.IngredientRepository
//import com.dishdiscoverers.foodrecipe.garett.model.Ingredient
//
//import io.realm.kotlin.Realm
//import io.realm.kotlin.notifications.ResultsChange
//import io.realm.kotlin.types.RealmUUID
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.launch
//
//abstract class IngredientRepositoryRealm : IngredientRepository {
//    lateinit var realm: Realm
//    private var currentJob: Job? = null
//
//    abstract suspend fun setupRealmSync()
//
//    /** Function to convert all the latest data in a Frog realm object into
//     *    a implementation-independent FrogData object so that it
//     *    can be read and displayed in the view
//     */
//    private fun cancelCurrentJob() {
//        currentJob?.cancel()
//        currentJob = null
//    }
//
//    suspend fun convertIngredientData(ingredient: Ingredient?) : IngredientData? {
//        if (!this::realm.isInitialized) {
//            setupRealmSync()
//        }
//
//        var ingredientData : IngredientData? = null
//        realm.write {
//            if (ingredient != null) {
//                ingredientData = IngredientData(
//                    id = ingredient!!.id,
//                    name= ingredient!!.name,
//                    category = ingredient!!.category,
//                    ingredient = ingredient)
//            }
//        }
//        return ingredientData
//    }
//
//    private fun closeRealmSync() {
//        realm.close()
//    }
//
//    /** Add a frog with the given data to the repository.
//     *    Initializes the id field to a random UUID if not specified.
//     *  Returns the new FrogData object or null if the operation failed. */
//
//    override suspend fun addIngredient(ingredientData : IngredientData): IngredientData? {
//        if (!this::realm.isInitialized) {
//            setupRealmSync()
//        }
//
//        var ingredient2: Ingredient? = null
//        realm.write {
//            // create a frog object in the realm
//            ingredient2 = this.copyToRealm(Ingredient().apply {
//                id = ingredientData.id ?: RealmUUID.random().toString()
//                name = ingredientData.name
//                category = ingredientData.category
//            })
//        }
//        return convertIngredientData(ingredient2)
//    }
//
//    /** Returns the first frog found that matches the given name *
//     *   Returns a FrogData if a match is found, null otherwise.
//     */
//    override suspend fun getIngredient(ingredientName : String): IngredientData? {
//        if (!this::realm.isInitialized) {
//            setupRealmSync()
//        }
//        cancelCurrentJob()
//        // Search equality on the primary key field name
//        val ingredient: Ingredient? = realm.query<Ingredient>(Ingredient::class, "name = \"$ingredientName\"").first().find()
//        return convertIngredientData(ingredient)
//    }
//
//    override suspend fun getAllIngredientsList(ingredientName: String?): List<IngredientData> {
//        if (!this::realm.isInitialized) {
//            setupRealmSync()
//        }
//        cancelCurrentJob()
//        val ingredients: List<Ingredient> =
//            if (ingredientName == null)
//                realm.query<Ingredient>(Ingredient::class).find()
//            else
//                realm.query<Ingredient>(Ingredient::class, "name = \"$ingredientName\"").find()
//        val list = ingredients.map {ingredient ->
//            IngredientData(
//                id = ingredient._id,
//                name = ingredient.name,
//                details = ingredient.details,
//                ingredient = ingredient
//            )
//        }
//
//        return list
//    }
//
//    override suspend fun getAllIngredients(ingredientName: String?): MutableState<List<IngredientData>> {
//        if (!this::realm.isInitialized) {
//            setupRealmSync()
//        }
//        val ingredientsState: MutableState<List<IngredientData>> = mutableStateOf(emptyList())
//        cancelCurrentJob()
//        currentJob = CoroutineScope(Dispatchers.Default).launch {
//            // Listen to the Realm query result as a Flow
//            val ingredientFlow: Flow<ResultsChange<Ingredient>> =
//                if (ingredientName == null)
//                    realm.query<Ingredient>(Ingredient::class).find().asFlow()
//                else
//                    realm.query<Ingredient>(Ingredient::class, "name = \"$ingredientName\"").find().asFlow()
//
//            ingredientFlow.collect { resultsChange: ResultsChange<Ingredient> ->
//                // Convert each Frog object to FrogData
//                val ingredients = resultsChange.list
//                val IngredientDataFlow = ingredients.map { ingredient ->
//                    IngredientData(
//                        id = ingredient._id,
//                        name = ingredient.name,
//                        details = ingredient.details,
//                        ingredient = ingredient
//                    )
//                }
//
//                // Update the mutable state with the new result
//                ingredientsState.value = IngredientDataFlow
//            }
//        }
//
//        return ingredientsState
//    }
//
//    override suspend fun getAllIngredientsFlow(ingredientName: String?): Flow<List<IngredientData>> {
//        if (!this::realm.isInitialized) {
//            setupRealmSync()
//        }
//        var ingredientsState: Flow<List<IngredientData>> = flowOf(emptyList())
//        cancelCurrentJob()
//
//        currentJob = CoroutineScope(Dispatchers.Default).launch {
//            val ingredientFlow: Flow<ResultsChange<Ingredient>> =
//                if (ingredientName == null)
//                    realm.query<Ingredient>(Ingredient::class).find().asFlow()
//                else
//                    realm.query<Ingredient>(Ingredient::class, "name = \"$ingredientName\"").find().asFlow()
//
//            ingredientFlow.collect{resultsChange: ResultsChange<Ingredient> ->
//                val ingredients = resultsChange.list
//                val ingredientDataFlow = ingredients.map {ingredient ->
//                    IngredientData(
//                        id = ingredient._id,
//                        name = ingredient.name,
//                        details = ingredient.details,
//                        ingredient = ingredient
//                    )
//                }
//                ingredientsState = flowOf(ingredientDataFlow)
//
//            }
//        }
//
//        return ingredientsState
//    }
//
//    override suspend fun updateIngredient(ingredientName: String, ingredientDetails: String): IngredientData? {
//        if (!this::realm.isInitialized) {
//            setupRealmSync()
//        }
//        var ingredientData: IngredientData? = null
//        try {
//            // Search equality on the primary key field name
//            val ingredient: Ingredient? =
//                realm.query<Ingredient>(Ingredient::class, "name = \"$ingredientName\"").first().find()
//
//            // delete one object synchronously
//            realm.write {
//                if (ingredient != null) {
//                    findLatest(ingredient)!!.details = ingredientDetails
//                }
//            }
//            ingredientData = convertIngredientData(ingredient)
//        } catch (e: Exception) {
//            println(e.message)
//        }
//
//        return ingredientData
//    }
//
//    /** Deletes the frog that is the first match to the given name
//     *   Returns the FrogData if a match is found, null otherwise.
//     */
//    override suspend fun deleteIngredient(ingredientName: String): IngredientData? {
//        if (!this::realm.isInitialized) {
//            setupRealmSync()
//        }
//        var ingredient2: IngredientData? = null
//        try {
//            // Search for first match on the name field
//            val ingredient: Ingredient? =
//                realm.query<Ingredient>(Ingredient::class, "name = \"$ingredientName\"").first().find()
//
//            realm.write {
//                if (ingredient != null) {
//                    // We need to extract the latest information about the frog
//                    //   before deleting it.  We can't call the convertToFrogData
//                    //   since we are in a realm.write block.
//                    ingredient2 = IngredientData(
//                        id = findLatest(ingredient)!!._id,
//                        name = findLatest(ingredient)!!.name,
//                        details = findLatest(ingredient)!!.details,
//                        ingredient = null)
//                    findLatest(ingredient)
//                        ?.also { delete(it) }
//                }
//            }
//        } catch (e: Exception) {
//            println(e.message)
//        }
//
//        return ingredient2
//    }
//}