package com.medapp.assistant.data.model

import androidx.compose.runtime.mutableStateListOf

sealed class MedicineCategory(val name: String) {
    object ANALGESICS : MedicineCategory("Анальгетики и антипиретики")
    object ANTIHYPERTENSIVE : MedicineCategory("Антигипертензивные средства")
    object CARDIOVASCULAR : MedicineCategory("Сердечно-сосудистые препараты")
    object ANTIALLERGIC : MedicineCategory("Противоаллергические средства")
    object ASTHMA : MedicineCategory("Препараты для неотложной помощи при астме")
    object ANTISEPTIC : MedicineCategory("Антисептики и дезинфицирующие средства")
    object FIRST_AID : MedicineCategory("Средства для оказания первой помощи")
    object DIGESTIVE : MedicineCategory("Средства для улучшения пищеварения и метаболизма")
    object DIURETICS : MedicineCategory("Диуретики")
    object SEDATIVE : MedicineCategory("Успокоительные и анксиолитики")

    companion object {
        fun values() = listOf(
            ANALGESICS, ANTIHYPERTENSIVE, CARDIOVASCULAR, ANTIALLERGIC,
            ASTHMA, ANTISEPTIC, FIRST_AID, DIGESTIVE, DIURETICS, SEDATIVE
        )
    }
}

// Препарат
data class Medicine(
    val name: String,
    val category: String,
    val forms: List<String>,
    val usage: String,
    val dosage: String,
    val expiry: String,
    val quantity: Int = 0 // Новое поле для количества
)

data class InventoryMedicine(
    val medicine: Medicine,
    val atHome: Boolean = false
)

object MedicineData {
    val categories = MedicineCategory.values()

    var medicines = listOf(
        Medicine(
            name = "Парацетамол",
            category = MedicineCategory.ANALGESICS.name,
            forms = listOf("таблетки"),
            usage = "Обезболивание, снижение температуры",
            dosage = "По 500 мг каждые 4-6 ч, макс. 4 г/24ч",
            expiry = "3 года"
        ),
        Medicine(
            name = "Ибупрофен",
            category = MedicineCategory.ANALGESICS.name,
            forms = listOf("таблетки", "суспензия"),
            usage = "Обезболивание, жаропонижение",
            dosage = "Взрослые: 200-800 мг 3 раза/день; дети: 5-10 мг/кг веса, каждые 6-8 ч",
            expiry = "2 года"
        ),
        Medicine(
            name = "Диклофенак натрия",
            category = MedicineCategory.ANALGESICS.name,
            forms = listOf("раствор для инъекций"),
            usage = "Миалгия, артриты, боли разной локализации",
            dosage = "В/м инъекции: 75 мг однократно или 25 мг х 3 р./день",
            expiry = "3 года"
        ),
        Medicine(
            name = "Каптоприл",
            category = MedicineCategory.ANTIHYPERTENSIVE.name,
            forms = listOf("таблетки"),
            usage = "Артериальная гипертония",
            dosage = "12,5-25 мг, возможно увеличение до 50 мг два-три раза в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Эналаприл",
            category = MedicineCategory.ANTIHYPERTENSIVE.name,
            forms = listOf("таблетки"),
            usage = "Гипертония, сердечная недостаточность",
            dosage = "5 мг один раз в сутки, постепенно увеличивая дозу до эффективной",
            expiry = "3 года"
        ),
        Medicine(
            name = "Амлодипин",
            category = MedicineCategory.ANTIHYPERTENSIVE.name,
            forms = listOf("таблетки"),
            usage = "Высокое артериальное давление",
            dosage = "5 мг один раз в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Нитроглицерин",
            category = MedicineCategory.CARDIOVASCULAR.name,
            forms = listOf("таблетки", "спрей"),
            usage = "Купирование приступов стенокардии",
            dosage = "Под язык по одной таблетке или одну дозу (0,4 мг) при болях в сердце/груди",
            expiry = "3 года"
        ),
        Medicine(
            name = "Атенолол",
            category = MedicineCategory.CARDIOVASCULAR.name,
            forms = listOf("таблетки"),
            usage = "Артериальная гипертензия, тахикардия",
            dosage = "25-50 мг ежедневно",
            expiry = "3 года"
        ),
        Medicine(
            name = "Бисопролол",
            category = MedicineCategory.CARDIOVASCULAR.name,
            forms = listOf("таблетки"),
            usage = "Повышенное давление, ишемическая болезнь сердца",
            dosage = "5 мг в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Супрастин",
            category = MedicineCategory.ANTIALLERGIC.name,
            forms = listOf("таблетки", "ампулы"),
            usage = "Аллергические заболевания, состояния",
            dosage = "Взрослым по 1 таб. (25 мг) 3-4 раза/сут; в/м: 1-2 мл разово, при необходимости повторно",
            expiry = "3 года"
        ),
        Medicine(
            name = "Лоратадин",
            category = MedicineCategory.ANTIALLERGIC.name,
            forms = listOf("таблетки"),
            usage = "Симптомы аллергии",
            dosage = "1 таблетка (10 мг) раз в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Цетиризин",
            category = MedicineCategory.ANTIALLERGIC.name,
            forms = listOf("капли", "таблетки"),
            usage = "Аллергический ринит, зуд кожи, проявления аллергии",
            dosage = "Детям 6-12 лет: 5 мг (10 кап.) в сутки; взрослым: 1 таб. (10 мг) раз в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Сальбутамол",
            category = MedicineCategory.ASTHMA.name,
            forms = listOf("аэрозоль"),
            usage = "Бронхиальная астма",
            dosage = "1-2 ингаляции при обострении симптомов",
            expiry = "3 года"
        ),
        Medicine(
            name = "Беродуал",
            category = MedicineCategory.ASTHMA.name,
            forms = listOf("раствор для ингаляций"),
            usage = "Приступ бронхиальной астмы",
            dosage = "1-2 мл для небулайзерной терапии, развести физраствором",
            expiry = "3 года"
        ),
        Medicine(
            name = "Хлоргексидин",
            category = MedicineCategory.ANTISEPTIC.name,
            forms = listOf("раствор"),
            usage = "Антисептическое средство, промывание ран и слизистых",
            dosage = "Применяют наружно, промывают раны и слизистые оболочки",
            expiry = "5 лет"
        ),
        Medicine(
            name = "Перекись водорода",
            category = MedicineCategory.ANTISEPTIC.name,
            forms = listOf("раствор"),
            usage = "Обработка ран",
            dosage = "Наружное использование для очищения раневых поверхностей",
            expiry = "2 года"
        ),
        Medicine(
            name = "Амоксициллин",
            category = MedicineCategory.FIRST_AID.name,
            forms = listOf("таблетки"),
            usage = "Инфекционно-воспалительные заболевания бактериального происхождения",
            dosage = "Взрослые: по 500 мг 3 раза в день, дети — 20-50 мг/кг массы тела в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Адреналин",
            category = MedicineCategory.FIRST_AID.name,
            forms = listOf("ампулы"),
            usage = "Экстренная помощь при анафилактическом шоке",
            dosage = "Введение подкожно, внутримышечно, индивидуально подобранная доза врачом",
            expiry = "3 года"
        ),
        Medicine(
            name = "Глюкоза",
            category = MedicineCategory.FIRST_AID.name,
            forms = listOf("раствор для внутривенного введения"),
            usage = "Вспомогательное средство при гипогликемии",
            dosage = "Индивидуально, зависит от тяжести состояния пациента",
            expiry = "3 года"
        ),
        Medicine(
            name = "Метоклопрамид",
            category = MedicineCategory.DIGESTIVE.name,
            forms = listOf("таблетки"),
            usage = "Тошнота, рвота",
            dosage = "Обычно по 10 мг 3 раза в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Домперидон",
            category = MedicineCategory.DIGESTIVE.name,
            forms = listOf("таблетки"),
            usage = "Улучшение моторики ЖКТ, тошнота, рвота",
            dosage = "Обычно по 10 мг 3 раза в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Витамин C",
            category = MedicineCategory.DIGESTIVE.name,
            forms = listOf("таблетки"),
            usage = "Поддержка иммунитета, антиоксидант",
            dosage = "По 500-1000 мг в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Витамин D",
            category = MedicineCategory.DIGESTIVE.name,
            forms = listOf("капли"),
            usage = "Регуляция кальциевого обмена",
            dosage = "Взрослым и детям: 1 капля (около 500 МЕ) в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Лазикс",
            category = MedicineCategory.DIURETICS.name,
            forms = listOf("таблетки"),
            usage = "Диуретическое средство, быстрое снижение АД",
            dosage = "По 20-40 мг внутрь 1 раз в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Валериана",
            category = MedicineCategory.SEDATIVE.name,
            forms = listOf("таблетки"),
            usage = "Седативное средство, тревога, бессонница",
            dosage = "Обычно по 1-2 таблетки 3 раза в сутки",
            expiry = "3 года"
        ),
        Medicine(
            name = "Феназепам",
            category = MedicineCategory.SEDATIVE.name,
            forms = listOf("таблетки"),
            usage = "Сильное успокоительное средство",
            dosage = "Только по назначению врача, индивидуальная дозировка",
            expiry = "3 года"
        )
    )

    // Новый список для отслеживаемых медикаментов
    var trackedMedicines = mutableStateListOf<InventoryMedicine>()
} 