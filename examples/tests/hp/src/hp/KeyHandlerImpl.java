package hp;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import hp.model.Capitulatus;
import hp.model.Diffindo;
import hp.model.Disaudio;
import hp.model.Expecto_Patronum;
import hp.model.Invito;
import hp.model.KeyHandler;
import hp.model.Levicorpus;
import hp.model.Locomotor;
import hp.model.MagicBox;
import hp.model.Protego;
import hp.model.Silencio;
import hp.model.Stupor;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Signal;

public class KeyHandlerImpl extends KeyHandler {

	MagicBox proxy;

	@SuppressWarnings("unchecked")
	Supplier<Signal>[] createSignal = (Supplier<Signal>[]) new Supplier[] { () -> new Capitulatus(), () -> new Diffindo(), () -> new Disaudio(),
			() -> new Expecto_Patronum(), () -> new Invito(), () -> new Levicorpus(), () -> new Locomotor(),
			() -> new Protego(), () -> new Silencio(), () -> new Stupor() };

	@Override
	public void init(MagicBox proxy) {
		this.proxy = proxy;
		
		// Disable jnativehook logging
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.err.println(e.getMessage());
			return;
		}

		GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

			char key;

			@Override
			public void nativeKeyTyped(NativeKeyEvent event) {
				key = event.getKeyChar();
			}

			@Override
			public void nativeKeyReleased(NativeKeyEvent event) {
				System.err.println("Received signal: " + key);
				try {
					int index = Integer.parseInt(Character.toString(key));
					Action.send(createSignal[index].get(), proxy);
				} catch (NumberFormatException e) {
				}
			}

			@Override
			public void nativeKeyPressed(NativeKeyEvent event) {
			}
		});
	}
}
