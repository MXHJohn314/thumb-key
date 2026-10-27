@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.dessalines.thumbkey.keyboards

import android.view.KeyEvent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.outlined.*
import com.dessalines.thumbkey.utils.*
import com.dessalines.thumbkey.utils.ColorVariant.*
import com.dessalines.thumbkey.utils.FontSizeVariant.*
import com.dessalines.thumbkey.utils.KeyAction.*
import com.dessalines.thumbkey.utils.SwipeNWay.*

// Medulla: a 4x4 layout with its controls along the top and the space bar in the
// middle row. Holding a letter key types its digit, and a swipe-return (when enabled)
// types a symbol's companion or a cursor move's bigger step.

private fun medullaEvent(
    keyCode: Int,
    metaState: Int = 0,
) = SendEvent(KeyEvent(0, 0, KeyEvent.ACTION_DOWN, keyCode, 0, metaState))

private val MEDULLA_ESCAPE_KEYC =
    KeyC(
        action = medullaEvent(KeyEvent.KEYCODE_ESCAPE),
        display = KeyDisplay.TextDisplay("esc"),
        color = MUTED,
    )

private val MEDULLA_BACKSPACE_KEY_ITEM =
    BACKSPACE_KEY_ITEM.copy(
        swipeType = EIGHT_WAY,
        topLeft = MEDULLA_ESCAPE_KEYC,
    )

private val MEDULLA_TOP_ROW =
    listOf(
        NUMERIC_KEY_ITEM,
        MEDULLA_BACKSPACE_KEY_ITEM,
        EMOJI_KEY_ITEM,
        RETURN_KEY_ITEM,
    )

private val MEDULLA_GO_KEYC =
    KeyC(
        action = IMECompleteAction,
        display = KeyDisplay.IconDisplay(Icons.AutoMirrored.Outlined.KeyboardReturn),
        color = MUTED,
    )

private val MEDULLA_NEW_LINE_KEYC = KeyC("\n", displayText = "↵", color = MUTED)

private val MEDULLA_TAB_KEYC =
    KeyC(
        action = medullaEvent(KeyEvent.KEYCODE_TAB),
        swipeReturnAction = medullaEvent(KeyEvent.KEYCODE_TAB, KeyEvent.META_SHIFT_ON),
        display = KeyDisplay.TextDisplay("⇥"),
        color = MUTED,
    )

private val MEDULLA_UP_KEYC =
    KeyC(
        action = medullaEvent(KeyEvent.KEYCODE_DPAD_UP),
        swipeReturnAction = medullaEvent(KeyEvent.KEYCODE_PAGE_UP),
        display = KeyDisplay.TextDisplay("↑"),
        color = MUTED,
    )

private val MEDULLA_DOWN_KEYC =
    KeyC(
        action = medullaEvent(KeyEvent.KEYCODE_DPAD_DOWN),
        swipeReturnAction = medullaEvent(KeyEvent.KEYCODE_PAGE_DOWN),
        display = KeyDisplay.TextDisplay("↓"),
        color = MUTED,
    )

private val MEDULLA_LEFT_KEYC =
    KeyC(
        action = medullaEvent(KeyEvent.KEYCODE_DPAD_LEFT),
        swipeReturnAction = PreviousWordBeforeCursor,
        display = KeyDisplay.TextDisplay("←"),
        color = MUTED,
    )

private val MEDULLA_RIGHT_KEYC =
    KeyC(
        action = medullaEvent(KeyEvent.KEYCODE_DPAD_RIGHT),
        swipeReturnAction = NextWordAfterCursor,
        display = KeyDisplay.TextDisplay("→"),
        color = MUTED,
    )

private fun medullaSymbol(
    text: String,
    returnText: String? = null,
) = KeyC(
    action = CommitText(text),
    swipeReturnAction = returnText?.let { CommitText(it) },
    display = KeyDisplay.TextDisplay(text),
    color = MUTED,
)

// The backspace in the letter rows, its b added per mode
private val MEDULLA_BACKSPACE_LETTER_KEY_ITEM =
    KeyItemC(
        center = BACKSPACE_KEYC,
        topLeft = medullaSymbol("}").copy(swipeReturnAction = DeleteWordBeforeCursor),
        top = medullaSymbol(")"),
        topRight = medullaSymbol("]").copy(swipeReturnAction = DeleteWordAfterCursor),
        right =
            KeyC(
                action = DeleteCharacterAfterCursor,
                swipeReturnAction = DeleteWordAfterCursor,
                display = KeyDisplay.TextDisplay("⌦"),
                color = MUTED,
            ),
        backgroundColor = SURFACE_VARIANT,
        longPress = DeleteKeyAction,
    )

private fun medullaKeyboard(shifted: Boolean): KeyboardC {
    fun l(letter: String) = KeyC(if (shifted) letter.uppercase() else letter)

    fun tap(letter: String) = KeyC(if (shifted) letter.uppercase() else letter, size = LARGE)

    val shiftUp = if (shifted) TOGGLE_CAPS_KEYC else TOGGLE_SHIFT_TRUE_KEYC
    val shiftDown =
        if (shifted) {
            TOGGLE_SHIFT_FALSE_KEYC
        } else {
            KeyC(
                ToggleShiftMode(false),
                swipeReturnAction = ToggleCurrentWordCapitalization(false),
            )
        }

    return KeyboardC(
        listOf(
            MEDULLA_TOP_ROW,
            listOf(
                KeyItemC(
                    center = tap("i"),
                    topLeft = medullaSymbol("["),
                    top = medullaSymbol("("),
                    topRight = medullaSymbol("{"),
                    bottomLeft = MEDULLA_NEW_LINE_KEYC,
                    bottom = MEDULLA_GO_KEYC,
                    bottomRight = l("f"),
                    longPress = CommitText("1"),
                ),
                KeyItemC(
                    center = tap("a"),
                    topLeft = medullaSymbol("'"),
                    top = medullaSymbol("!", "¡"),
                    topRight = medullaSymbol("/", "—"),
                    bottomLeft = l("w"),
                    bottom = l("y"),
                    longPress = CommitText("2"),
                ),
                KeyItemC(
                    center = tap("n"),
                    topLeft = medullaSymbol("\\", "|"),
                    top = medullaSymbol("?", "¿"),
                    topRight = medullaSymbol("\""),
                    bottomLeft = l("v"),
                    bottom = l("p"),
                    bottomRight = l("x"),
                    longPress = CommitText("3"),
                ),
                MEDULLA_BACKSPACE_LETTER_KEY_ITEM.copy(bottomLeft = l("b")),
            ),
            listOf(
                KeyItemC(
                    center = tap("t"),
                    left = UNDO_KEYC,
                    right = REDO_KEYC,
                    top = shiftUp,
                    topRight = medullaSymbol("~"),
                    bottom = shiftDown,
                    bottomLeft = medullaSymbol("<"),
                    bottomRight = medullaSymbol("_", "¬"),
                    longPress = CommitText("4"),
                ),
                KeyItemC(
                    center = SPACEBAR_CENTER_KEYC,
                    topLeft = medullaSymbol(";"),
                    top = l("h"),
                    topRight = medullaSymbol(":", ","),
                    left = l("c"),
                    right = l("m"),
                    bottomLeft = medullaSymbol(","),
                    bottom = l("g"),
                    bottomRight = medullaSymbol(".", "…"),
                    backgroundColor = SURFACE_VARIANT,
                    widthMultiplier = 2,
                ),
                KeyItemC(
                    center = tap("r"),
                    left = medullaSymbol("|"),
                    topLeft = l("q"),
                    top = shiftUp,
                    topRight = MEDULLA_TAB_KEYC,
                    right = medullaSymbol("`", "\\"),
                    bottom = shiftDown,
                    bottomLeft = medullaSymbol("*", "†"),
                    bottomRight = medullaSymbol(">"),
                    longPress = CommitText("0"),
                ),
            ),
            listOf(
                KeyItemC(
                    center = tap("d"),
                    top = MEDULLA_UP_KEYC,
                    topRight = l("j"),
                    left = MEDULLA_LEFT_KEYC,
                    right = MEDULLA_RIGHT_KEYC,
                    bottomLeft = medullaSymbol("=", "±"),
                    bottom = MEDULLA_DOWN_KEYC,
                    bottomRight = medullaSymbol("+", "×"),
                    longPress = CommitText("7"),
                ),
                KeyItemC(
                    center = tap("e"),
                    top = l("u"),
                    bottomLeft = medullaSymbol("-", "+"),
                    bottom = medullaSymbol("@"),
                    bottomRight = medullaSymbol("#"),
                    longPress = CommitText("8"),
                ),
                KeyItemC(
                    center = tap("o"),
                    topLeft = l("z"),
                    top = l("l"),
                    bottomLeft = medullaSymbol("$", "¢"),
                    bottom = medullaSymbol("%", "‰"),
                    bottomRight = medullaSymbol("^"),
                    longPress = CommitText("9"),
                ),
                KeyItemC(
                    center = tap("s"),
                    topLeft = l("k"),
                    top = MEDULLA_UP_KEYC,
                    left = MEDULLA_LEFT_KEYC,
                    right = MEDULLA_RIGHT_KEYC,
                    bottomLeft = medullaSymbol("&"),
                    bottom = MEDULLA_DOWN_KEYC,
                    longPress = CommitText("0"),
                ),
            ),
        ),
    )
}

val KB_EN_MEDULLA_MAIN = medullaKeyboard(shifted = false)

val KB_EN_MEDULLA_SHIFTED = medullaKeyboard(shifted = true)

val KB_EN_MEDULLA: KeyboardDefinition =
    KeyboardDefinition(
        title = "english medulla",
        modes =
            KeyboardDefinitionModes(
                main = KB_EN_MEDULLA_MAIN,
                shifted = KB_EN_MEDULLA_SHIFTED,
                numeric = NUMERIC_KEYBOARD,
            ),
        settings =
            KeyboardDefinitionSettings(
                autoCapitalizers = arrayOf(::autoCapitalizeI, ::autoCapitalizeIApostrophe),
            ),
    )
