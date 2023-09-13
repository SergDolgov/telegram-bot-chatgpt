package org.example.chatgpt;

import org.example.chatgpt.message.Message;

import java.io.IOException;import java.util.List;

public interface Conversation extends AutoCloseable {
  Message ask(String question) throws IOException, InterruptedException;

  String id();

  List<Message> messages();

  void save() throws IOException;

  void load() throws IOException;
}
