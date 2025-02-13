/*
 * Copyright 2019-2020 Aitu Software Limited.
 *
 * https://aitusoftware.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aitusoftware.babl.websocket;

final public class NoOpSessionDataListener extends SessionDataListener {

  @Override
  public void sendDataAvailable() {

  }

  @Override
  public void sendDataProcessed() {

  }

  @Override
  public void receiveDataAvailable() {

  }

  @Override
  public void receiveDataProcessed() {

  }

  @Override
  public void sessionClosed() {

  }

  @Override
  public void init(final long sessionId) {

  }
}
