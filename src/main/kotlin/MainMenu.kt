package project // Ensure this matches your project's package structure

// Core Compose & Foundation Imports
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState

// Animation Imports
import androidx.compose.animation.core.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState

// Material Design Imports
import androidx.compose.material.*

// Runtime Imports (State, Effects, etc.)
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap // Required for mutableStateMapOf

// UI Imports (Modifiers, Alignment, etc.)
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.draw.clipToBounds

// Graphics Imports (Geometry, Color, Brush, etc.)
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp // Import the lerp function for Color

// Text Imports
import androidx.compose.ui.text.style.TextAlign

// Unit Imports (dp, sp)
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Utility Imports
import androidx.compose.ui.util.lerp // Required for interpolation
import kotlin.random.Random // Required for randomizing animations
import kotlin.math.min // If minOf isn't available directly (usually is)

// Coroutine Imports
import kotlinx.coroutines.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// --- Color Definitions ---
private val libraryColors = listOf(
    Color(0xFF5D4037), Color(0xFF795548), Color(0xFF8D6E63), Color(0xFF4E342E),
    Color(0xFF3E2723), Color(0xFF6A1B9A).copy(alpha = 0.8f), Color(0xFF4A148C).copy(alpha = 0.8f),
    Color(0xFF1B5E20).copy(alpha = 0.8f), Color(0xFFBF360C).copy(alpha = 0.8f), Color(0xFF870000).copy(alpha = 0.7f)
)
private val shelfColor = Color(0xFF3E2723)
private val emptyShelfColor = shelfColor.copy(alpha = 0.6f) // Color when book is picked


// --- State/Data Class Definitions ---

// Enum to represent the animation state of a book
enum class BookAnimation {
    IDLE,       // Normal state (spine visible)
    PICKING,    // Fading out (being picked)
    PICKED,     // Completely gone (off the shelf)
    RETURNING,  // Fading back in (being returned)
    OPENING,    // Widening/brightening slightly (being opened)
    CLOSING     // Reverting from OPENING to IDLE
}

// Holds the static visual properties of a book slot on the shelf
data class BookSlot(
    val id: Int,
    val x: Float, // Represents top-left X in dp units for initial calc
    val y: Float, // Represents top-left Y in dp units for initial calc
    val width: Float, // Represents width in dp units for initial calc
    val height: Float, // Represents height in dp units for initial calc
    val baseColor: Color
)

// Holds the dynamic animation state for a specific book slot
class BookAnimationState(
    initialState: BookAnimation = BookAnimation.IDLE,
    initialProgress: Float = 0f
) {
    var state by mutableStateOf(initialState)
    val progress = Animatable(initialProgress) // Use Animatable for smooth transitions

    suspend fun animateTo(targetState: BookAnimation, duration: Int = 400) {
        val startProgress = progress.value
        val targetProgress = when (targetState) {
            BookAnimation.IDLE -> 0f
            BookAnimation.PICKED -> 1f
            BookAnimation.OPENING -> 1f
            BookAnimation.PICKING -> 1f
            BookAnimation.RETURNING -> 1f
            BookAnimation.CLOSING -> 0f
        }

        state = targetState

        val shouldAnimateProgress = when (state) {
            BookAnimation.PICKING, BookAnimation.OPENING -> startProgress != 1f
            BookAnimation.RETURNING -> startProgress != 1f
            BookAnimation.CLOSING -> startProgress != 0f
            else -> false
        }

        if (shouldAnimateProgress) {
            progress.animateTo(
                targetValue = targetProgress,
                animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing)
            )
        } else if (state == BookAnimation.IDLE || state == BookAnimation.PICKED) {
            progress.snapTo(targetProgress)
        }
    }

    suspend fun snapTo(targetState: BookAnimation, targetProgress: Float = 0f) {
        state = targetState
        progress.snapTo(targetProgress)
    }
}


// --- Animated Background Composable ---

@Composable
fun AnimatedLibraryBackground(modifier: Modifier = Modifier) {
    val random = remember { Random(System.currentTimeMillis()) }

    // Generate book slots and animation states
    val bookSlots = remember {
        val slots = mutableListOf<BookSlot>()
        val shelfHeight = 80.dp.value
        val bookHeightRatio = 0.85f
        val bookThicknessMin = 8.dp.value
        val bookThicknessMax = 25.dp.value
        val screenHeightEstimate = 800.dp.value
        val screenWidthEstimate = 800.dp.value // Adjusted to cover the entire window width
        val numShelves = (screenHeightEstimate / shelfHeight).toInt() + 4

        var currentY = -shelfHeight * 2
        for (shelfIndex in 0 until numShelves) {
            var currentX = -bookThicknessMax
            var bookIdCounter = shelfIndex * 1000

            while (currentX < screenWidthEstimate + bookThicknessMax) {
                val bookThickness = random.nextFloat() * (bookThicknessMax - bookThicknessMin) + bookThicknessMin
                slots.add(
                    BookSlot(
                        id = bookIdCounter++,
                        x = currentX,
                        y = currentY + (shelfHeight * (1 - bookHeightRatio)) / 2,
                        width = bookThickness * 0.9f,
                        height = shelfHeight * bookHeightRatio,
                        baseColor = libraryColors[random.nextInt(libraryColors.size)]
                    )
                )
                currentX += bookThickness
            }
            currentY += shelfHeight
        }
        slots
    }

    val animationStates = remember {
        mutableStateMapOf<Int, BookAnimationState>().apply {
            bookSlots.forEach { put(it.id, BookAnimationState()) }
        }
    }

    // Add more complex animation logic
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(random.nextLong(500, 2000))

            val idleBookIds = animationStates.filter { it.value.state == BookAnimation.IDLE }.keys.toList()

            if (idleBookIds.isNotEmpty()) {
                val count = minOf(idleBookIds.size, random.nextInt(3, 7))
                val idsToAnimate = idleBookIds.shuffled(random).take(count)

                idsToAnimate.forEach { bookId ->
                    val animState = animationStates[bookId] ?: return@forEach

                    launch {
                        if (random.nextFloat() < 0.5f) {
                            animState.animateTo(BookAnimation.PICKING)
                            animState.snapTo(BookAnimation.PICKED, 1f)
                            delay(random.nextLong(1000, 3000))
                            animState.animateTo(BookAnimation.RETURNING)
                            animState.snapTo(BookAnimation.IDLE, 0f)
                        } else {
                            animState.animateTo(BookAnimation.OPENING)
                            delay(random.nextLong(800, 2500))
                            animState.animateTo(BookAnimation.CLOSING)
                            animState.snapTo(BookAnimation.IDLE, 0f)
                        }
                    }
                }
            }
        }
    }

    // Larger and smoother pulsating glow effects, positioned at the right extreme
    val glowAlpha = rememberInfiniteTransition().animateFloat(
        initialValue = 0.1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glowScale = rememberInfiniteTransition().animateFloat(
        initialValue = 1.0f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = modifier
            .clipToBounds()
            .blur(radius = 8.dp)
    ) {
        val shelfHeightPx = 80.dp.toPx()
        val shelfThicknessPx = 4.dp.toPx()

        // Draw a single larger glow at the right extreme with 80% opacity
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xA770F5).copy(alpha = glowAlpha.value * 0.8f), // Adjusted to 80% opacity
                    Color.Transparent
                ),
                center = Offset(size.width, size.height * 0.5f), // Positioned at the right extreme
                radius = size.minDimension / 2 * glowScale.value
            ),
            size = size
        )

        // Draw shelves
        val distinctYPositionsPx = bookSlots.map { it.y.dp.toPx() }.distinct().sorted()
        distinctYPositionsPx.forEach { bookYPx ->
            val slotHeightPx = bookSlots.first { it.y.dp.toPx() == bookYPx }.height.dp.toPx()
            val shelfYPx = bookYPx + slotHeightPx
            drawRect(
                color = shelfColor,
                topLeft = Offset(0f, shelfYPx + shelfThicknessPx / 2),
                size = Size(size.width, shelfThicknessPx)
            )
        }

        // Draw books
        bookSlots.forEach { slot ->
            val animState = animationStates[slot.id] ?: return@forEach

            val slotXPx = slot.x.dp.toPx()
            val slotYPx = slot.y.dp.toPx()
            val slotWidthPx = slot.width.dp.toPx()
            val slotHeightPx = slot.height.dp.toPx()

            val progress = animState.progress.value
            var currentAlpha = 1f
            var currentWidthPx = slotWidthPx
            var currentColor = slot.baseColor
            var drawColor = currentColor

            when (animState.state) {
                BookAnimation.IDLE -> {}
                BookAnimation.PICKING -> currentAlpha = 1f - progress
                BookAnimation.PICKED -> {
                    currentAlpha = 0f
                    drawColor = emptyShelfColor
                }
                BookAnimation.RETURNING -> {
                    currentAlpha = progress
                    drawRect(
                        color = emptyShelfColor,
                        topLeft = Offset(slotXPx, slotYPx),
                        size = Size(slotWidthPx, slotHeightPx)
                    )
                }
                BookAnimation.OPENING -> {
                    currentWidthPx = lerp(slotWidthPx, slotWidthPx * 1.3f, progress)
                    currentColor = lerp(slot.baseColor, Color.White.copy(alpha = 0.6f), progress)
                    drawColor = currentColor
                }
                BookAnimation.CLOSING -> {
                    currentWidthPx = lerp(slotWidthPx, slotWidthPx * 1.3f, progress)
                    currentColor = lerp(slot.baseColor, Color.White.copy(alpha = 0.6f), progress)
                    drawColor = currentColor
                }
            }

            if (animState.state == BookAnimation.PICKED) {
                drawRect(
                    color = emptyShelfColor,
                    topLeft = Offset(slotXPx, slotYPx),
                    size = Size(slotWidthPx, slotHeightPx)
                )
            }

            val widthDifferencePx = currentWidthPx - slotWidthPx
            val adjustedXPx = slotXPx - widthDifferencePx / 2

            drawRect(
                color = drawColor,
                topLeft = Offset(adjustedXPx, slotYPx),
                size = Size(currentWidthPx, slotHeightPx),
                alpha = currentAlpha
            )
        }
    }
}


// --- Window Background Composable ---
@Composable
fun WindowBackground(backgroundType: String, modifier: Modifier = Modifier) {
    when (backgroundType) {
        "main" -> AnimatedLibraryBackground(modifier)
        "books" -> {
            // Example: blue gradient background for book-related screens
            Box(
                modifier = modifier.background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF283E51), Color(0xFF485563))
                    )
                )
            )
        }
        "members" -> {
            // Example: green gradient background for member-related screens
            Box(
                modifier = modifier.background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF56ab2f), Color(0xFFA8E063))
                    )
                )
            )
        }
        "events" -> {
            // Example: orange gradient background for event-related screens
            Box(
                modifier = modifier.background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFB347), Color(0xFFFFCC33))
                    )
                )
            )
        }
        else -> {
            // Default fallback background
            Box(modifier = modifier.background(Color(0xFF121212)))
        }
    }
}


// --- Main Menu Composable ---
@Composable
fun MainMenu(onNavigate: (String) -> Unit) {
    MaterialTheme(
        colors = darkColors(
            primary = Color(0xFF6200EE),
            surface = Color(0xFF121212),
            background = Color(0xFF121212),
            onPrimary = Color.White,
            onBackground = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            WindowBackground("main", modifier = Modifier.fillMaxSize())

            // Center all content (title + buttons) both vertically and horizontally
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Library Management System",
                        color = Color.White,
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    val buttons = listOf(
                        "Add Book" to "AddBook", "Register Member" to "RegisterMember", "List Available Books" to "ListAvailableBooks",
                        "Borrow Book" to "BorrowBook", "Return Book" to "ReturnBook", "Search Books" to "SearchBooks",
                        "List Transactions" to "ListTransactions", "List Overdue Books" to "ListOverdueBooks", "Generate Report" to "GenerateReport",
                        "Notify Overdue Members" to "NotifyOverdueMembers", "View Statistics" to "ViewStatistics", "Add Book Review" to "AddBookReview",
                        "View Book Reviews" to "ViewBookReviews", "Reset Library" to "ResetLibrary", "Calculate Fines" to "CalculateFines",
                        "Recommend Books" to "RecommendBooks", "Manage Events" to "ManageEvents",
                        "Download Book" to "DownloadBook"
                    )
                    val buttonWidth = 200.dp
                    val buttonHeight = 50.dp
                    val columns = 3

                    buttons.chunked(columns).forEach { rowButtons ->
                        Row(
                            // Remove fillMaxWidth and use only needed padding for centering
                            modifier = Modifier.padding(vertical = 20.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowButtons.forEach { (label, screen) ->
                                val interactionSource = remember { MutableInteractionSource() }
                                val isHovered by interactionSource.collectIsHoveredAsState()
                                val scale by animateFloatAsState(
                                    targetValue = if (isHovered) 1.05f else 1f,
                                    animationSpec = tween(durationMillis = 150)
                                )
                                val shadowElevation by animateDpAsState(
                                    targetValue = if (isHovered) 4.dp else 2.dp,
                                    animationSpec = tween(durationMillis = 150)
                                )
                                val glowAlpha by animateFloatAsState(
                                    targetValue = if (isHovered) 0.75f else 0f,
                                    animationSpec = tween(durationMillis = 250)
                                )

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                        .width(buttonWidth)
                                        .height(buttonHeight)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .graphicsLayer(
                                                scaleX = 1.18f,
                                                scaleY = 1.28f,
                                                alpha = glowAlpha
                                            )
                                            .background(
                                                brush = Brush.radialGradient(
                                                    colors = listOf(
                                                        Color(0xFF98FF98).copy(alpha = 0.85f),
                                                        Color(0xFF98FF98).copy(alpha = 0.001f),
                                                        Color.Transparent
                                                    ),
                                                    radius = 180f
                                                ),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                    )
                                    Button(
                                        onClick = { onNavigate(screen) },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                            .graphicsLayer(
                                                scaleX = scale,
                                                scaleY = scale,
                                                shadowElevation = shadowElevation.value
                                            ),
                                        shape = RoundedCornerShape(8.dp),
                                        interactionSource = interactionSource,
                                        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp)
                                    ) {
                                        Text(
                                            text = label,
                                            color = MaterialTheme.colors.onPrimary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
