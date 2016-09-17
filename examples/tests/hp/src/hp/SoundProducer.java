package hp;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import hp.model.Sound;

public class SoundProducer extends Sound {

	@Override
	public void Expecto_Patronum() {
		playSound("expecto_patronum.wav");
	}

	@Override
	public void Protego() {
		playSound("protego.wav");
	}

	@Override
	public void Invito() {
		playSound("invito.wav");
	}

	@Override
	public void Locomotor() {
		playSound("locomotor.wav");
	}

	@Override
	public void Disaudio() {
		playSound("disaudio.wav");
	}

	@Override
	public void Capitulatus() {
		playSound("capitulatus.wav");
	}

	@Override
	public void Diffindo() {
		playSound("diffindo.wav");
	}

	@Override
	public void Silencio() {
		playSound("silencio.wav");
	}

	@Override
	public void Stupor() {
		playSound("stupor.wav");
	}

	@Override
	public void Levicorpus() {
		playSound("levicorpus.wav");
	}

	private void playSound(String fileName) {
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File("resources/high/" + fileName));
			Clip clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
			clip.open(stream);
			clip.addLineListener(new LineListener() {

				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP) {
						clip.close();
					}
				}
			});
			clip.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void Tadaa() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		playSound("tadaa.wav");
	}
}
