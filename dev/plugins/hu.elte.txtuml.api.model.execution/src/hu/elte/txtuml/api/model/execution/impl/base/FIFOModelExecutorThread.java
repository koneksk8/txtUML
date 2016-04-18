package hu.elte.txtuml.api.model.execution.impl.base;

import java.util.concurrent.LinkedBlockingQueue;

import hu.elte.txtuml.api.model.Signal;

/**
 * A model executor thread implementation which has a FIFO mailbox, processing
 * events in the order in which they were raised.
 * <p>
 * <b>Implementation note:</b> this thread uses a {@link LinkedBlockingQueue} as
 * its mailbox with {@link Runnable} entries one of which is taken and run when
 * the {@link #processNext} method is called.
 */
public class FIFOModelExecutorThread extends ModelExecutorThread {

	private final LinkedBlockingQueue<Runnable> mailbox = new LinkedBlockingQueue<>();

	public FIFOModelExecutorThread(AbstractModelExecutor<?> executor, AbstractRuntime<?, ?> runtime,
			Runnable initialization) {
		super(executor, runtime, initialization);
	}

	@Override
	protected void processNext() throws InterruptedException {
		mailbox.take().run();
	}

	@Override
	public boolean isEmpty() {
		return mailbox.isEmpty();
	}

	@Override
	public void wake() {
		mailbox.add(new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	@Override
	public void send(final Signal signal, final AbstractPortWrapper target) {
		addEntry(new Runnable() {
			@Override
			public void run() {
				target.receive(signal);
			}
		});
	}

	@Override
	public void send(final Signal signal, final AbstractModelClassWrapper target) {
		addEntry(new Runnable() {
			@Override
			public void run() {
				target.receive(signal);
			}
		});
	}

	@Override
	public void send(final Signal signal, final AbstractPortWrapper sender, final AbstractPortWrapper target) {
		addEntry(new Runnable() {
			@Override
			public void run() {
				target.receive(signal, sender);
			}
		});
	}

	@Override
	public void send(final Signal signal, final AbstractPortWrapper sender, final AbstractModelClassWrapper target) {
		addEntry(new Runnable() {
			@Override
			public void run() {
				target.receive(signal, sender);
			}
		});
	}

	/**
	 * Adds a new entry to the FIFO mailbox of this thread.
	 * <p>
	 * Thread-safe.
	 */
	protected void addEntry(Runnable toPerform) {
		mailbox.add(toPerform);
	}

}
