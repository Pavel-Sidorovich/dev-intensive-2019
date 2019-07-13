package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val temp: String = question.check(answer)
        if (temp == "null") {
            if (question.answers.contains(answer)) {
                question = question.nextQuestion()
                return "Отлично - ты справился\n${question.question}" to status.color
            } else {
                if (status == Status.CRITICAL) {
                    status = status.nextStatus()
                    return "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                } else
                    status = status.nextStatus()
                return "Это неправильный ответ\n${question.question}" to status.color
            }
        } else return "$temp${question.question}" to status.color
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else values()[0]
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "Bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun check(answer: String): String {
                return if (answer[0] != answer[0].toLowerCase()) {
                    "null"
                } else
                    "Имя должно начинаться с заглавной буквы\n"
            }
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun check(answer: String): String {
                return if (answer[0] != answer[0].toUpperCase()) {
                    "null"
                } else
                    "Профессия должна начинаться со строчной буквы\n"
            }
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun check(answer: String): String {
                return if (Regex("\\D+").find(answer)?.value == answer) {
                    "null"
                } else
                    "Материал не должен содержать цифр\n"
            }
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun check(answer: String): String {
                return if (Regex("\\d+").find(answer)?.value == answer ) {
                    "null"
                } else
                    "Год моего рождения должен содержать только цифры\n"
            }
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun check(answer: String): String {
                return if (Regex("\\d{7}").find(answer)?.value == answer ) {
                    "null"
                } else
                    "Серийный номер содержит только цифры, и их 7\n"
            }
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun check(answer: String): String {
                return ""
            }
        };

        abstract fun nextQuestion(): Question
        abstract fun check(answer: String): String
    }


}