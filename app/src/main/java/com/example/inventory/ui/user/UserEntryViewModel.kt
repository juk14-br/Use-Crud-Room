/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.User
import java.text.NumberFormat

/**
 * ViewModel to validate and insert items in the Room database.
 */
class UserEntryViewModel : ViewModel() {

    /**
     * Holds current item ui state
     */
    var userUiState by mutableStateOf(UserUiState())
        private set

    /**
     * Updates the [userUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(userDetails: UserDetails) {
        userUiState =
            UserUiState(userDetails = userDetails, isEntryValid = validateInput(userDetails))
    }

    private fun validateInput(uiState: UserDetails = userUiState.userDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && email.isNotBlank() && idade.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false
)

data class UserDetails(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val idade: String = "",
)

/**
 * Extension function to convert [UserDetails] to [User]. If the value of [UserDetails.email] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [UserDetails.idade] is not a valid [Int], then the quantity will be set to 0
 */
fun UserDetails.toItem(): User = User(
    id = id,
    name = name,
    price = email.toDoubleOrNull() ?: 0.0,
    idade = idade.toIntOrNull() ?: 0
)

fun User.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

/**
 * Extension function to convert [User] to [UserUiState]
 */
fun User.toItemUiState(isEntryValid: Boolean = false): UserUiState = UserUiState(
    userDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [User] to [UserDetails]
 */
fun User.toItemDetails(): UserDetails = UserDetails(
    id = id,
    name = name,
    email = price.toString(),
    idade = idade.toString()
)
