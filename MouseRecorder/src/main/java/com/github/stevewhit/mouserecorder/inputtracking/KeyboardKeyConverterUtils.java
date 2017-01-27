package com.github.stevewhit.mouserecorder.inputtracking;

import java.awt.event.KeyEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

public class KeyboardKeyConverterUtils
{
	/**
	 * Private constructor to keep this as a static utility class.
	 */
	private KeyboardKeyConverterUtils(){}
	
	/**
	 * Returns the EventKey value of a native key keycode.
	 * @param nativeKeyCode The native key keycode int.
	 * @return Returns the Event key equivelant value of the native key keycode; otherwise it returns -1 if it can't find it.
	 */
	public static int nativeKeyToEventKey(NativeKeyEvent event)
	{
		switch(event.getKeyCode())
		{
			case(NativeKeyEvent.VC_ESCAPE) : return KeyEvent.VK_ESCAPE;
			
			case(NativeKeyEvent.VC_F1) : return KeyEvent.VK_F1;
			case(NativeKeyEvent.VC_F2) : return KeyEvent.VK_F2;
			case(NativeKeyEvent.VC_F3) : return KeyEvent.VK_F3;
			case(NativeKeyEvent.VC_F4) : return KeyEvent.VK_F4;
			case(NativeKeyEvent.VC_F5) : return KeyEvent.VK_F5;
			case(NativeKeyEvent.VC_F6) : return KeyEvent.VK_F6;
			case(NativeKeyEvent.VC_F7) : return KeyEvent.VK_F7;
			case(NativeKeyEvent.VC_F8) : return KeyEvent.VK_F8;
			case(NativeKeyEvent.VC_F9) : return KeyEvent.VK_F9;
			case(NativeKeyEvent.VC_F10) : return KeyEvent.VK_F10;
			case(NativeKeyEvent.VC_F11) : return KeyEvent.VK_F11;
			case(NativeKeyEvent.VC_F12) : return KeyEvent.VK_F12;
			
			case(NativeKeyEvent.VC_BACKQUOTE) : return KeyEvent.VK_BACK_QUOTE;
			
			case(NativeKeyEvent.VC_1) : return KeyEvent.VK_1;
			case(NativeKeyEvent.VC_2) : return KeyEvent.VK_2;
			case(NativeKeyEvent.VC_3) : return KeyEvent.VK_3;
			case(NativeKeyEvent.VC_4) : return KeyEvent.VK_4;
			case(NativeKeyEvent.VC_5) : return KeyEvent.VK_5;
			case(NativeKeyEvent.VC_6) : return KeyEvent.VK_6;
			case(NativeKeyEvent.VC_7) : return KeyEvent.VK_7;
			case(NativeKeyEvent.VC_8) : return KeyEvent.VK_8;
			case(NativeKeyEvent.VC_9) : return KeyEvent.VK_9;
			case(NativeKeyEvent.VC_0) : return KeyEvent.VK_0;
			
			case(NativeKeyEvent.VC_ENTER) : return KeyEvent.VK_ENTER;
			case(NativeKeyEvent.VC_MINUS) : return KeyEvent.VK_MINUS;
			case(NativeKeyEvent.VC_EQUALS) : return KeyEvent.VK_EQUALS;
			case(NativeKeyEvent.VC_BACKSPACE) : return KeyEvent.VK_BACK_SPACE;
			case(NativeKeyEvent.VC_TAB) : return KeyEvent.VK_TAB;
			case(NativeKeyEvent.VC_CAPS_LOCK) : return KeyEvent.VK_CAPS_LOCK;
			case(NativeKeyEvent.VC_SHIFT_L) : return KeyEvent.VK_SHIFT;
			case(NativeKeyEvent.VC_SHIFT_R) : return KeyEvent.VK_SHIFT;
			case(NativeKeyEvent.VC_CONTROL_L) : return KeyEvent.VK_CONTROL;
			case(NativeKeyEvent.VC_CONTROL_R) : return KeyEvent.VK_CONTROL;
			case(NativeKeyEvent.VC_OPEN_BRACKET) : return KeyEvent.VK_OPEN_BRACKET;
			case(NativeKeyEvent.VC_CLOSE_BRACKET) : return KeyEvent.VK_CLOSE_BRACKET;
			case(NativeKeyEvent.VC_BACK_SLASH) : return KeyEvent.VK_BACK_SLASH;
			case(NativeKeyEvent.VC_SEMICOLON) : return KeyEvent.VK_SEMICOLON;
			case(NativeKeyEvent.VC_QUOTE) : return KeyEvent.VK_QUOTE;
			case(NativeKeyEvent.VC_SLASH) : return KeyEvent.VK_SLASH;
			case(NativeKeyEvent.VC_COMMA) : return KeyEvent.VK_COMMA;
			case(NativeKeyEvent.VC_PERIOD) : return KeyEvent.VK_PERIOD;
			case(NativeKeyEvent.VC_SPACE) : return KeyEvent.VK_SPACE;
			case(NativeKeyEvent.VC_ALT_L) : return KeyEvent.VK_ALT;
			case(NativeKeyEvent.VC_ALT_R) : return KeyEvent.VK_ALT;
			case(NativeKeyEvent.VC_PAGE_UP) : return KeyEvent.VK_PAGE_UP;
			case(NativeKeyEvent.VC_PAGE_DOWN) : return KeyEvent.VK_PAGE_DOWN;
			case(NativeKeyEvent.VC_PRINTSCREEN) : return KeyEvent.VK_PRINTSCREEN;
			case(NativeKeyEvent.VC_SCROLL_LOCK) : return KeyEvent.VK_SCROLL_LOCK;
			case(NativeKeyEvent.VC_PAUSE) : return KeyEvent.VK_PAUSE;
			case(NativeKeyEvent.VC_INSERT) : return KeyEvent.VK_INSERT;
			case(NativeKeyEvent.VC_DELETE) : return KeyEvent.VK_DELETE;
			case(NativeKeyEvent.VC_HOME) : return KeyEvent.VK_HOME;
			case(NativeKeyEvent.VC_END) : return KeyEvent.VK_END;
			case(NativeKeyEvent.VC_NUM_LOCK) : return KeyEvent.VK_NUM_LOCK;
			case(NativeKeyEvent.VC_UP) : return KeyEvent.VK_UP;
			case(NativeKeyEvent.VC_DOWN) : return KeyEvent.VK_DOWN;
			case(NativeKeyEvent.VC_LEFT) : return KeyEvent.VK_LEFT;
			case(NativeKeyEvent.VC_RIGHT) : return KeyEvent.VK_RIGHT;
			
			case(NativeKeyEvent.VC_META_L) : return KeyEvent.VK_META;
			case(NativeKeyEvent.VC_META_R) : return KeyEvent.VK_META;
			
			case(NativeKeyEvent.VC_KP_1) : return KeyEvent.VK_0;
			case(NativeKeyEvent.VC_KP_2) : return KeyEvent.VK_1;
			case(NativeKeyEvent.VC_KP_3) : return KeyEvent.VK_2;
			case(NativeKeyEvent.VC_KP_4) : return KeyEvent.VK_3;
			case(NativeKeyEvent.VC_KP_5) : return KeyEvent.VK_4;
			case(NativeKeyEvent.VC_KP_6) : return KeyEvent.VK_5;
			case(NativeKeyEvent.VC_KP_7) : return KeyEvent.VK_6;
			case(NativeKeyEvent.VC_KP_8) : return KeyEvent.VK_7;
			case(NativeKeyEvent.VC_KP_9) : return KeyEvent.VK_8;
			case(NativeKeyEvent.VC_KP_0) : return KeyEvent.VK_9;
			case(NativeKeyEvent.VC_KP_DIVIDE) : return KeyEvent.VK_DIVIDE;
			case(NativeKeyEvent.VC_KP_MULTIPLY) : return KeyEvent.VK_MULTIPLY;
			case(NativeKeyEvent.VC_KP_SUBTRACT) : return KeyEvent.VK_SUBTRACT;
			case(NativeKeyEvent.VC_KP_ADD) : return KeyEvent.VK_ADD;
			case(NativeKeyEvent.VC_KP_ENTER) : return KeyEvent.VK_ENTER;
			case(NativeKeyEvent.VC_KP_EQUALS) : return KeyEvent.VK_EQUALS;
			
			case(NativeKeyEvent.VC_A) : return KeyEvent.VK_A;
			case(NativeKeyEvent.VC_B) : return KeyEvent.VK_B;
			case(NativeKeyEvent.VC_C) : return KeyEvent.VK_C;
			case(NativeKeyEvent.VC_D) : return KeyEvent.VK_D;
			case(NativeKeyEvent.VC_E) : return KeyEvent.VK_E;
			case(NativeKeyEvent.VC_F) : return KeyEvent.VK_F;
			case(NativeKeyEvent.VC_G) : return KeyEvent.VK_G;
			case(NativeKeyEvent.VC_H) : return KeyEvent.VK_H;
			case(NativeKeyEvent.VC_I) : return KeyEvent.VK_I;
			case(NativeKeyEvent.VC_J) : return KeyEvent.VK_J;
			case(NativeKeyEvent.VC_K) : return KeyEvent.VK_K;
			case(NativeKeyEvent.VC_L) : return KeyEvent.VK_L;
			case(NativeKeyEvent.VC_M) : return KeyEvent.VK_M;
			case(NativeKeyEvent.VC_N) : return KeyEvent.VK_N;
			case(NativeKeyEvent.VC_O) : return KeyEvent.VK_O;
			case(NativeKeyEvent.VC_P) : return KeyEvent.VK_P;
			case(NativeKeyEvent.VC_Q) : return KeyEvent.VK_Q;
			case(NativeKeyEvent.VC_R) : return KeyEvent.VK_R;
			case(NativeKeyEvent.VC_S) : return KeyEvent.VK_S;
			case(NativeKeyEvent.VC_T) : return KeyEvent.VK_T;
			case(NativeKeyEvent.VC_U) : return KeyEvent.VK_U;
			case(NativeKeyEvent.VC_V) : return KeyEvent.VK_V;
			case(NativeKeyEvent.VC_W) : return KeyEvent.VK_W;
			case(NativeKeyEvent.VC_X) : return KeyEvent.VK_X;
			case(NativeKeyEvent.VC_Y) : return KeyEvent.VK_Y;
			case(NativeKeyEvent.VC_Z) : return KeyEvent.VK_Z;
		}
		
		return -1;
	}
}














