package com.mabdelhamid.illamovies.base

interface Mapper<INPUT, OUTPUT> {

    fun map(input: INPUT): OUTPUT

    fun mapList(input: List<INPUT>): List<OUTPUT> {
        return input.map { map(it) }
    }
}