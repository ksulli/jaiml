/*
    jaiml - java AIML library
    Copyright (C) 2004, 2009  Kim Sullivan

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package aiml.context;

import java.util.EmptyStackException;

import aiml.context.data.EnvironmentInputSource;
import aiml.environment.Environment;

public class EnvironmentInputContext extends InputContext {

  public EnvironmentInputContext(String name) {
    super(name, new EnvironmentInputSource());
  }

  public void push(String input, Environment e) {
    e.pushInput(input);
  }

  public void pop(Environment e) throws EmptyStackException {
    e.popInput();
  }
}
