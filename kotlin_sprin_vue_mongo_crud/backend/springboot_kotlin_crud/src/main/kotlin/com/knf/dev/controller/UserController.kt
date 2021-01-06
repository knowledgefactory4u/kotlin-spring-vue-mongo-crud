package com.knf.dev.controller


import com.knf.dev.model.User
import com.knf.dev.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = arrayOf("http://localhost:8080"))
class UserController(private val userRepository: UserRepository) {

    @GetMapping("/users")
    fun getAllUsers(): List<User> =
            userRepository.findAll()

    @PostMapping("/users")
    fun createNewUser(@RequestBody user: User): User =
            userRepository.save(user)

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable(value = "id") userId: String): ResponseEntity<User> {
        return userRepository.findById(userId).map { user ->
            ResponseEntity.ok(user)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/users/{id}")
    fun updateUserById(@PathVariable(value = "id") userId: String,
                       @RequestBody newUser: User): ResponseEntity<User> {

        return userRepository.findById(userId).map { existingUser ->
            val updatedUser: User = existingUser
                    .copy(firstName = newUser.firstName, lastName = newUser.lastName, emailId = newUser.emailId)
            ResponseEntity.ok().body(userRepository.save(updatedUser))
        }.orElse(ResponseEntity.notFound().build())

    }

    @DeleteMapping("/users/{id}")
    fun deleteUserById(@PathVariable(value = "id") userId: String): ResponseEntity<Void> {
        return userRepository.findById(userId).map { user ->
            userRepository.delete(user)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }
}