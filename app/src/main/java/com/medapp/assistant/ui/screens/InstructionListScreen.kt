package com.medapp.assistant.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.compose.currentBackStackEntryAsState
import com.medapp.assistant.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

// Данные для инструкций
val instructions = listOf(
    InstructionData(
        number = 1,
        title = "Ожог кипятком",
        description = "Молодой человек случайно пролил на руку кипящую воду при заваривании чая.",
        imageRes = R.drawable.ozhog_kipyatkom
    ),
    InstructionData(
        number = 2,
        title = "Порез",
        description = "Девушка резала авокадо и случайно порезалась.",
        imageRes = R.drawable.porez
    ),
    InstructionData(
        number = 3,
        title = "Укус собаки",
        description = "Мальчика укусила бездомная собака на детской площадке.",
        imageRes = R.drawable.ukus_sobaki
    ),
    InstructionData(
        number = 4,
        title = "Оказание помощи при удушье",
        description = "В ресторане у женщины застрял кусок пищи в горле, возникли признаки удушья.",
        imageRes = R.drawable.udushie
    ),
    InstructionData(
        number = 5,
        title = "Солнечный удар",
        description = "Мужчина долго находился под открытым солнцем, почувствовал слабость, головокружение, возможна потеря сознания.",
        imageRes = R.drawable.solnechniy_udar
    ),
    InstructionData(
        number = 6,
        title = "Перелом",
        description = "Ребёнок упал с турника во дворе, жалуется на сильную боль в руке, подозрение на перелом.",
        imageRes = R.drawable.perelom
    ),
    InstructionData(
        number = 7,
        title = "Аллергическая реакция",
        description = "У девушки начался сильный отёк губ и затруднённое дыхание после употребления ореха.",
        imageRes = R.drawable.allergiya
    ),
    InstructionData(
        number = 8,
        title = "Обморожение",
        description = "Мальчик обморозил руки, находясь на улице без перчаток.",
        imageRes = R.drawable.obmorozhenie
    )
)

data class InstructionData(
    val number: Int,
    val title: String,
    val description: String,
    val imageRes: Int
)

data class InstructionDetail(
    val number: Int,
    val title: String,
    val imageRes: Int,
    val shortDescription: String,
    val steps: List<String>,
    val donts: List<String> = emptyList(),
    val tips: List<String> = emptyList(),
    val warnings: List<String> = emptyList()
)

val instructionDetails = listOf(
    InstructionDetail(
        number = 1,
        title = "Ожог кипятком",
        imageRes = R.drawable.ozhog_kipyatkom,
        shortDescription = "Молодой человек случайно пролил на руку кипящую воду при заваривании чая.",
        steps = listOf(
            "Уберите источник горячей воды.",
            "Остудите поражённый участок под прохладной (не ледяной!) водой 10-20 минут.",
            "Снимите украшения и одежду, если они не прилипли к коже.",
            "Накройте ожог стерильной влажной повязкой или чистой тканью.",
            "При сильной боли дайте обезболивающее (например, парацетамол)."
        ),
        donts = listOf(
            "Не смазывайте ожог маслом, кремами, спиртом, йодом.",
            "Не вскрывайте пузыри.",
            "Не используйте лед."
        ),
        warnings = listOf(
            "Если ожог обширный, на лице, паху или сопровождается сильной болью — обратитесь к врачу!"
        )
    ),
    InstructionDetail(
        number = 2,
        title = "Порез",
        imageRes = R.drawable.porez,
        shortDescription = "Девушка резала авокадо и случайно порезалась.",
        steps = listOf(
            "Промойте рану под проточной водой.",
            "Остановите кровь, прижав чистой салфеткой или бинтом.",
            "Обработайте края раны антисептиком (не лейте в саму рану).",
            "Наложите стерильную повязку.",
            "При необходимости обратитесь к врачу (глубокий порез, сильное кровотечение)."
        ),
        donts = listOf(
            "Не используйте вату для остановки кровотечения.",
            "Не оставляйте рану открытой."
        ),
        warnings = listOf(
            "Если рана глубокая, края расходятся или не удаётся остановить кровь — обратитесь к врачу!"
        )
    ),
    InstructionDetail(
        number = 3,
        title = "Укус собаки",
        imageRes = R.drawable.ukus_sobaki,
        shortDescription = "Мальчика укусила бездомная собака на детской площадке.",
        steps = listOf(
            "Промойте рану большим количеством воды с мылом.",
            "Обработайте края антисептиком.",
            "Остановите кровь, наложите стерильную повязку.",
            "Обратитесь к врачу для профилактики бешенства и столбняка!"
        ),
        donts = listOf(
            "Не прижигайте и не зашивайте рану самостоятельно.",
            "Не используйте агрессивные антисептики внутрь раны."
        ),
        warnings = listOf(
            "Всегда обращайтесь к врачу после укуса животного!"
        )
    ),
    InstructionDetail(
        number = 4,
        title = "Оказание помощи при удушье",
        imageRes = R.drawable.udushie,
        shortDescription = "В ресторане у женщины застрял кусок пищи в горле, возникли признаки удушья.",
        steps = listOf(
            "Попросите человека покашлять, если он может дышать и говорить.",
            "Если не может — встаньте сзади, обхватите руками под рёбра и выполните приём Геймлиха (резко надавите внутрь и вверх).",
            "Повторяйте до освобождения дыхательных путей или приезда скорой помощи.",
            "Если человек потерял сознание — вызовите скорую и начните СЛР."
        ),
        donts = listOf(
            "Не бейте по спине, если человек может кашлять.",
            "Не пытайтесь достать предмет пальцами, если не видите его."
        ),
        warnings = listOf(
            "Если человек не дышит — срочно вызывайте скорую!"
        )
    ),
    InstructionDetail(
        number = 5,
        title = "Солнечный удар",
        imageRes = R.drawable.solnechniy_udar,
        shortDescription = "Мужчина долго находился под открытым солнцем, почувствовал слабость, головокружение, возможна потеря сознания.",
        steps = listOf(
            "Уведите пострадавшего в тень или прохладное место.",
            "Уложите с приподнятой головой.",
            "Дайте прохладную воду небольшими глотками.",
            "Охладите тело: приложите влажную ткань, обмахивайте, используйте вентилятор.",
            "Следите за состоянием, при потере сознания — вызовите скорую."
        ),
        donts = listOf(
            "Не давайте алкоголь и кофе.",
            "Не оставляйте пострадавшего одного."
        ),
        warnings = listOf(
            "Если человек теряет сознание или не реагирует — срочно вызовите скорую!"
        )
    ),
    InstructionDetail(
        number = 6,
        title = "Перелом",
        imageRes = R.drawable.perelom,
        shortDescription = "Ребёнок упал с турника во дворе, жалуется на сильную боль в руке, подозрение на перелом.",
        steps = listOf(
            "Обеспечьте покой повреждённой конечности.",
            "Иммобилизируйте (зафиксируйте) конечность подручными средствами (шина, доска, платок).",
            "Приложите холод к месту травмы.",
            "Дайте обезболивающее при необходимости.",
            "Доставьте пострадавшего к врачу."
        ),
        donts = listOf(
            "Не пытайтесь вправлять кость самостоятельно.",
            "Не давайте пострадавшему двигаться."
        ),
        warnings = listOf(
            "При открытом переломе не пытайтесь вправлять кость и не удаляйте предметы из раны!"
        )
    ),
    InstructionDetail(
        number = 7,
        title = "Аллергическая реакция",
        imageRes = R.drawable.allergiya,
        shortDescription = "У девушки начался сильный отёк губ и затруднённое дыхание после употребления ореха.",
        steps = listOf(
            "Срочно вызовите скорую помощь!",
            "Дайте антигистаминный препарат (если есть).",
            "Усадите пострадавшего, обеспечьте доступ свежего воздуха.",
            "Если есть автоинъектор адреналина — используйте его по инструкции."
        ),
        donts = listOf(
            "Не оставляйте пострадавшего одного.",
            "Не давайте пищу и воду при затруднённом дыхании."
        ),
        warnings = listOf(
            "Отёк Квинке и анафилаксия — угроза жизни! Срочно вызовите скорую."
        )
    ),
    InstructionDetail(
        number = 8,
        title = "Обморожение",
        imageRes = R.drawable.obmorozhenie,
        shortDescription = "Мальчик обморозил руки, находясь на улице без перчаток.",
        steps = listOf(
            "Заведите пострадавшего в тепло.",
            "Снимите мокрую одежду и наденьте сухую.",
            "Постепенно согревайте поражённые участки (тёплая вода 37-39°C, сухое тепло).",
            "Дайте тёплое питьё.",
            "Обратитесь к врачу при сильном обморожении."
        ),
        donts = listOf(
            "Не растирайте снегом или спиртом.",
            "Не используйте горячую воду или грелки."
        ),
        warnings = listOf(
            "При появлении пузырей, потере чувствительности — срочно к врачу!"
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Инструкции первой помощи", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {
            Card(
                onClick = { navController.navigate("quiz_detail/1") },
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(bottom = 18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Quiz, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(18.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Тест по первой помощи", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("15 вопросов для проверки знаний первой помощи.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                items(instructions) { instruction ->
                    InstructionCard(instruction = instruction, onClick = {
                        navController.navigate("instruction_detail/${instruction.number}")
                    })
                }
            }
        }
    }
}

@Composable
fun InstructionCard(instruction: InstructionData, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = instruction.imageRes),
                contentDescription = instruction.title,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = instruction.number.toString().padStart(2, '0'),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                )
                Text(
                    text = instruction.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                )
                Text(
                    text = instruction.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Экран подробной инструкции
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionDetailScreen(
    instructionNumber: Int,
    navController: NavController
) {
    val detail = instructionDetails.firstOrNull { it.number == instructionNumber }
    if (detail == null) {
        // Ошибка: инструкция не найдена
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Инструкция не найдена", color = MaterialTheme.colorScheme.error)
        }
        return
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(detail.title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Image(
                painter = painterResource(id = detail.imageRes),
                contentDescription = detail.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(18.dp))
            )
            Text(detail.shortDescription, style = MaterialTheme.typography.bodyLarge)
            Section(title = "Что делать", icon = Icons.Default.Check, color = MaterialTheme.colorScheme.primary) {
                detail.steps.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
            }
            if (detail.donts.isNotEmpty()) {
                Section(title = "Чего нельзя делать", icon = Icons.Default.Block, color = MaterialTheme.colorScheme.error) {
                    detail.donts.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
                }
            }
            if (detail.tips.isNotEmpty()) {
                Section(title = "Советы", icon = Icons.Default.Info, color = MaterialTheme.colorScheme.secondary) {
                    detail.tips.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
                }
            }
            if (detail.warnings.isNotEmpty()) {
                Section(title = "Внимание", icon = Icons.Default.Warning, color = MaterialTheme.colorScheme.error) {
                    detail.warnings.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error) }
                }
            }
        }
    }
}

@Composable
fun Section(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color, content: @Composable ColumnScope.() -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = color)
    }
    Column(content = content, modifier = Modifier.padding(start = 30.dp))
} 