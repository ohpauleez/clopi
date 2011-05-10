
clopi : Statistics on Clojure packages and libraries
=======================================================

When searching for third-party Clojure libs, I often find myself asking:
    Which package should I be using?
I usually base my decision on a lot of things (documentation, functionality, etc),  
but one piece that I find hard determing is how many other projects are using a specific library.

Clopi was built to tell me the dependency statistics of clojure libraries from various data sources.



Usage and Examples
==================

TODO



Installation
============

### With Leiningen or Cake

TODO

### Via Source

TODO


TODO
====

* Create necessary features for web based and API based search (rank score by dep count)
* Augment search results with Github or BitBucket activity
* Integrate clojure metrics project, to also include basic metrics
* Allow search filtering (eg: nothing depends on it, has more than 10k lines)
 * If nothing depends on it, and it doesn't share a name with another project, it's an application
 * If nothing depends on it, and it shares a name of something that has nothing depends on, it's a forked application
 * If nothing depends on it, but it shares a name of something that things depend on, it's a forked lib
 * If things depend on it, it's a library


Hacking
=======

### Using TMUX or screen

If you want to start a new tmux server, cd to the project root dir, just type:
    tmux -f tmux-screen/clopi.tmux.conf
otherwise you can start a new session:
    tmux `cat tmux-screen/clopi.tmux.newsession`
You can also use screen:
    screen -c tmux-screen/clopi-screenrc -S clopi


### VimClojure tips

Start a nailgun (which will also open a repl),
run the following command from the project root:
    script/nailgun
You can also run a rlwrap'd REPL that VimClojure can connect to:
    lein repl
And then call the Nailgun server function:
    (runnail)
or more commonly,
    (def nail (runnail))

Here are helpful commands
    * \rt - run tests in the given namespace
    * \lw - lookup word
    * \li - lookup interactive
    * \gw - goto word
    * \sw - source lookup word
    * \el - eval line
    * \ep - eval paragraph
    * \ef - eval file
    * \me - macroexpand
    * \m1 - macroexpand1
    * \rf - require the file

### Paredit.vim tips

I also make use of the paredit.vim file from the slimv.vim plugin. This assumes your <leader> is \
    * :call ToggleParedit - toggle it on and off.
    * \W wrap in paren (works with visual selection too)
    * \J join paren - (a)(b) -> (a b)
    * \O split paren - (a b) -> (a)(b)
    * \S splice paren - ((a b)) -> (a b)
Wrapping can also be tailored, and used on a visual block:
    * \w"
    * \w[
    * \w(


### CDT - Clojure Debugging toolkit

You can start a debugging REPL with: `lein cdt`
    (set-catch java.lang.IllegalArgumentException :all)
    (delete-catch java.lang.IllegalArgumentException)
    (print-frames)
    (up)
    (down)
    (cont)
    (set-bp clojure.core/into)
    (delete-bp clojure.core/into)
    (local-names)
    (locals)
    (reval)
    (reval-println)


### Running the tests

From the project root, `./script/lazytest` will run all the tests.
To start a test watcher, `./script/lazytest-watch`


License
=======

    Copyright (C) 2010 Paul deGrandis. All rights reserved.
    Distributed under the Eclipse Public License, the same as Clojure.
	
	The use and distribution terms for this software are covered by the 
	Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) 
	which can be found in the file LICENSE.html at the root of this distribution. 
	By using this software in any fashion, you are agreeing to be bound by the terms of this license. 
	You must not remove this notice, or any other, from this software.

