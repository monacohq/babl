package com.aitusoftware.babl.websocket;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.agrona.collections.LongArrayQueue;

/**
 * {@code ConnectionValidator} that will always succeed.
 */
public final class LogHeaderValidator implements ConnectionValidator
{
  private final LongArrayQueue pendingValidations = new LongArrayQueue(128, Long.MIN_VALUE);
  private final ValidationResult reusableResult = new ValidationResult();
  private ValidationResultPublisher validationResultPublisher;

  {

    reusableResult.validationSuccess();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void validateConnection(
      final ValidationResult validationResult,
      final Consumer<BiConsumer<CharSequence, CharSequence>> headerProvider,
      final ValidationResultPublisher validationResultPublisher)
  {
//        final String threadName = Thread.currentThread().getName();
//        if (!threadName.startsWith("[babl-session-adapter-")) {
//            System.err.println("Validator should only get called with babl session adapter threads");
//            System.exit(1);
//        }
//        if (threadName.charAt("[babl-session-adapter-".length() + 1) != ',') {
//            System.err.println("Validator should only support instances thread at max 10 only!");
//            System.exit(1);
//        }
//        int threadId = threadName.charAt("[babl-session-adapter-".length()) - '0';
//        System.out.println(threadId);
    if (this.validationResultPublisher == null)
    {
      this.validationResultPublisher = validationResultPublisher;
    }
    headerProvider.accept((k, v) -> {
      System.out.println(k.toString() + " :: " + v.toString());
    } );

    validationResult.validationSuccess();
    if (!validationResultPublisher.publishResult(validationResult))
    {
      pendingValidations.addLong(validationResult.sessionId());
    }
    else
    {
      publishPendingValidations(validationResultPublisher);
    }
  }

  @Override
  public int doWork()
  {
    return publishPendingValidations(validationResultPublisher);
  }

  private int publishPendingValidations(final ValidationResultPublisher validationResultPublisher)
  {
    int workDone = 0;
    for (int i = 0; i < pendingValidations.size(); i++)
    {
      final long pendingSessionId = pendingValidations.peekLong();
      reusableResult.sessionId(pendingSessionId);
      if (validationResultPublisher.publishResult(reusableResult))
      {
        pendingValidations.pollLong();
        workDone++;
      }
      else
      {
        break;
      }
    }
    return workDone;
  }
}