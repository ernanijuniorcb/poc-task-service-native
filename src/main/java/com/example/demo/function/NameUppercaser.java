package com.example.demo.function;

import com.example.demo.domain.TaskEntity;

import java.util.function.Function;

// @Component
public class NameUppercaser implements Function<TaskEntity, String> {

  @Override
  public String apply(TaskEntity input) {
    return "hi " + input.getEid().toUpperCase() + "!";
  }
}
