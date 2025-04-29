package com.medapp.assistant.data.model

import com.google.gson.annotations.SerializedName

data class Quiz(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("questions")
    val questions: List<Question>,
    
    @SerializedName("passingScore")
    val passingScore: Int, // percentage needed to pass
    
    @SerializedName("isOffline")
    val isOffline: Boolean = false,
    
    @SerializedName("lastUpdated")
    val lastUpdated: Long = System.currentTimeMillis()
) {
    data class Question(
        @SerializedName("id")
        val id: Long,
        
        @SerializedName("text")
        val text: String,
        
        @SerializedName("options")
        val options: List<String>,
        
        @SerializedName("correctAnswer")
        val correctAnswer: Int, // index of correct option
        
        @SerializedName("explanation")
        val explanation: String
    )
}

val BasicFirstAidQuiz = Quiz(
    id = 1L,
    title = "Базовый тест первой помощи",
    description = "",
    category = "Первая помощь",
    questions = listOf(
        Quiz.Question(
            id = 1L,
            text = "Боец ранен в груди, свистящее дыхание. Что делать?",
            options = listOf(
                "Заткнуть рану в груди.",
                "Уколоть обезболивающее.",
                "Наложить герметичную повязку на рану в груди, проверить вентиляцию.",
                "Дать воды."
            ),
            correctAnswer = 2,
            explanation = "Правильный ответ: наложить герметичную повязку и проверить вентиляцию."
        ),
        Quiz.Question(
            id = 2L,
            text = "Перелом, но пульс ниже раны слабый. Что делать?",
            options = listOf(
                "Не трогать, ждать врачей.",
                "Фиксировать как есть.",
                "Попробовать выровнять кость, проверить пульс.",
                "Помолиться."
            ),
            correctAnswer = 0,
            explanation = "Правильный ответ: не трогать, ждать врачей."
        ),
        Quiz.Question(
            id = 3L,
            text = "Аллергия, отек Квинке. Что колоть?",
            options = listOf(
                "Обезболивающее.",
                "Антибиотик.",
                "Эпинефрин(адреналин).",
                "Ничего не колоть."
            ),
            correctAnswer = 2,
            explanation = "Правильный ответ: эпинефрин (адреналин)."
        ),
        Quiz.Question(
            id = 4L,
            text = "Сбилось дыхание после взрыва. Что предпринять?",
            options = listOf(
                "Похлопать по спине.",
                "Дать понюхать нашатырь.",
                "Проверить на пневмоторакс.",
                "Просто отдохнуть."
            ),
            correctAnswer = 2,
            explanation = "Правильный ответ: проверить на пневмоторакс."
        ),
        Quiz.Question(
            id = 5L,
            text = "Глубокое переохлаждение, но дышит. Что первым делом?",
            options = listOf(
                "Снять мокрую одежду.",
                "Дать горячий чай.",
                "Растирать.",
                "Раздеться и греть телом."
            ),
            correctAnswer = 0,
            explanation = "Правильный ответ: снять мокрую одежду."
        ),
        Quiz.Question(
            id = 6L,
            text = "Какой антибиотик предпочтительнее при ранении мягких тканей (если выбор есть)?",
            options = listOf(
                "Амоксициллин.",
                "Ципрофлоксацин.",
                "Доксициклин.",
                "Метронидазол."
            ),
            correctAnswer = 0,
            explanation = "Правильный ответ: амоксициллин."
        ),
        Quiz.Question(
            id = 7L,
            text = "Теряет кровь, но в сознании. Что даём пить?",
            options = listOf(
                "Воду.",
                "Крепкий чай.",
                "Кофе.",
                "Ничего."
            ),
            correctAnswer = 1,
            explanation = "Правильный ответ: крепкий чай."
        ),
        Quiz.Question(
            id = 8L,
            text = "Сломал ногу, торчит кость. Что нельзя делать?",
            options = listOf(
                "Обезболить.",
                "Наложить шину.",
                "Вправлять кость обратно.",
                "Остановить кровотечение."
            ),
            correctAnswer = 2,
            explanation = "Правильный ответ: нельзя вправлять кость обратно."
        ),
        Quiz.Question(
            id = 9L,
            text = "У товарища началась эпилепсия что делать?",
            options = listOf(
                "Пытаться остановить судороги.",
                "Засунуть что-то в рот.",
                "Убрать все опасные предметы, следить чтобы не поранился.",
                "Перетянуть конечности турникетами."
            ),
            correctAnswer = 2,
            explanation = "Правильный ответ: убрать опасные предметы, следить чтобы не поранился."
        ),
        Quiz.Question(
            id = 10L,
            text = "После жгута конечность синеет. Что дальше?",
            options = listOf(
                "Не ослаблять, ждать медиков.",
                "Ослабить и наложить снова выше.",
                "Ослабить, если нет кровотечения.",
                "Все плохо, ампутация."
            ),
            correctAnswer = 1,
            explanation = "Правильный ответ: ослабить и наложить снова выше."
        ),
        Quiz.Question(
            id = 11L,
            text = "Потерялся в бреду. Какая ранняя помощь?",
            options = listOf(
                "Связать.",
                "Успокоить, перебинтовать голову.",
                "Искать причину.",
                "Дать шлепок."
            ),
            correctAnswer = 1,
            explanation = "Правильный ответ: успокоить, перебинтовать голову."
        ),
        Quiz.Question(
            id = 12L,
            text = "Сам ранен, но есть возможность помочь товарищу. Что предпринять?",
            options = listOf(
                "Заняться собой.",
                "Помочь товарищу, если это не угрожает твоей жизни.",
                "Ждать помощи вместе.",
                "Стрелять в воздух."
            ),
            correctAnswer = 1,
            explanation = "Правильный ответ: помочь товарищу, если это не угрожает твоей жизни."
        ),
        Quiz.Question(
            id = 13L,
            text = "Кончились бинты. Замена?",
            options = listOf(
                "Футболка.",
                "Скотч.",
                "Листья.",
                "Ничего."
            ),
            correctAnswer = 0,
            explanation = "Правильный ответ: футболка."
        ),
        Quiz.Question(
            id = 14L,
            text = "Аптечка с истекшим сроком. Что делать?",
            options = listOf(
                "Использовать.",
                "Сообщить, использовать необходимое.",
                "Выбросить.",
                "Продлить срок."
            ),
            correctAnswer = 1,
            explanation = "Правильный ответ: сообщить, использовать необходимое."
        ),
        Quiz.Question(
            id = 15L,
            text = "Просит только обезболивающее. Что делаем?",
            options = listOf(
                "Колем.",
                "Осмотреть, потом колоть.",
                "Сначала кровь, потом обезболивающее.",
                "Ничего не колем."
            ),
            correctAnswer = 1,
            explanation = "Правильный ответ: осмотреть, потом колоть."
        )
    ),
    passingScore = 80
) 