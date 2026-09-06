/*
 * Copyright 2026 Lscythe
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
package dev.lscythe.app.navigo.convention

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class TranslationParityTask : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceFile: RegularFileProperty

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val translationFile: RegularFileProperty

    @TaskAction
    fun checkParity() {
        val source = readStrings(sourceFile.get().asFile)
        val translation = readStrings(translationFile.get().asFile)
        check(source.keys == translation.keys) {
            "Translation keys differ. Missing: ${source.keys - translation.keys}; extra: ${translation.keys - source.keys}"
        }

        source.forEach { (key, value) ->
            check(placeholders(value) == placeholders(translation.getValue(key))) {
                "Format placeholders differ for $key"
            }
        }
    }

    private fun readStrings(file: File): Map<String, String> {
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val nodes = document.getElementsByTagName("string")
        return buildMap {
            repeat(nodes.length) { index ->
                val node = nodes.item(index)
                if (node.attributes.getNamedItem("translatable")?.nodeValue != "false") {
                    put(node.attributes.getNamedItem("name").nodeValue, node.textContent)
                }
            }
        }
    }

    private fun placeholders(value: String): List<String> =
        PLACEHOLDER.findAll(value).map { it.value }.toList()

    private companion object {
        val PLACEHOLDER = Regex("%\\d+\\$[a-zA-Z]")
    }
}
