/*
 * Copyright 2019, OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.metrics;

import io.opentelemetry.metrics.ObserverLong.BoundLongObserver;
import io.opentelemetry.metrics.ObserverLong.ResultLongObserver;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Observer to report pre-aggregated metrics with double value.
 *
 * <p>Example:
 *
 * <pre>{@code
 * class YourClass {
 *
 *   private static final Meter meter = OpenTelemetry.getMeterFactory().get("my_library_name");
 *   private static final ObserverLong observer =
 *       meter.
 *           .observerLongBuilder("doWork_latency")
 *           .setDescription("gRPC Latency")
 *           .setUnit("ms")
 *           .build();
 *
 *   void init() {
 *     observer.setCallback(
 *         new ObserverLong.Callback<ObserverLong.Result>() {
 *           final AtomicInteger count = new AtomicInteger(0);
 *          {@literal @}Override
 *           public void update(ResultLongObserver result) {
 *             result.put(observer.getDefaultBound(), count.addAndGet(1));
 *           }
 *         });
 *   }
 * }
 * }</pre>
 *
 * @since 0.1.0
 */
@ThreadSafe
public interface ObserverLong extends Observer<ResultLongObserver, BoundLongObserver> {
  @Override
  BoundLongObserver bind(LabelSet labelSet);

  @Override
  BoundLongObserver getDefaultBound();

  @Override
  void unbind(BoundLongObserver bound);

  @Override
  void setCallback(Callback<ResultLongObserver> metricUpdater);

  /** Builder class for {@link ObserverLong}. */
  interface Builder extends Observer.Builder<ObserverLong.Builder, ObserverLong> {}

  /**
   * A {@code Bound} for a {@code Observer}.
   *
   * @since 0.1.0
   */
  @ThreadSafe
  interface BoundLongObserver {}

  /** The result for the {@link io.opentelemetry.metrics.Observer.Callback}. */
  interface ResultLongObserver {
    void put(BoundLongObserver bound, long value);
  }
}
