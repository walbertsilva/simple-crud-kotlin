package com.example.bankaccount

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("accounts")
class AccountContoller(val repository: AccountRepository) {
    @PostMapping
    fun create(@RequestBody account: Account) = ResponseEntity.ok(repository.save(account));

    @GetMapping
    fun read() = ResponseEntity.ok(repository.findAll())

    @GetMapping("{document}")
    fun readByDocument(@PathVariable document: String) = ResponseEntity.ok(repository.findByDocument(document))

    @PutMapping("{document}")
    fun update(@PathVariable document: String, @RequestBody account: Account): ResponseEntity<Account> {
        val accountDbOptional = repository.findByDocument(document)
        val accountDb = accountDbOptional.orElseThrow { RuntimeException("Acciunt document: $document not found!") }
        val saved = repository.save(accountDb.copy(name = account.name, balance = account.balance))

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{document}")
    fun delete(@PathVariable document: String) = repository
            .findByDocument(document)
            .ifPresent { repository.delete(it) }
}