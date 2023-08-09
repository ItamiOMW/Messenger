package com.example.itami_chat.core.data.preferences.user

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreUserManager @Inject constructor(
    @ApplicationContext context: Context,
) : UserManager {

    private val dataStore = DataStoreFactory.create(DataStoreUserSerializer, produceFile = {
        context.dataStoreFile(USER_DATASTORE_NAME)
    })

    override suspend fun setUser(user: DataStoreUser?) {
        dataStore.updateData {
            user ?: DataStoreUser() //If the user is null then insert default DataStoreUser
        }
    }

    override val user: Flow<DataStoreUser>
        get() = dataStore.data


    companion object {

        private const val USER_DATASTORE_NAME = "user_datastore"

    }
}
