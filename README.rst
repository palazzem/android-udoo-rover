==================
Android UDOO rover
==================

Android app with ADK support to send/receive commands to/from Arduino rover with MC33926 motor shield. Powered by UDOO prototyping board.

External dependencies
---------------------

* `ADK toolkit`_ (Gradle/Maven)

Arduino compatibility
---------------------

This Android application uses ``ADK 2012`` to communicate with an enabled ADK Arduino.
Arduino receives serial commands from Android app to pilot a motor driver.
It uses `ADK toolkit`_ library to wrap standard ADK features.

An `Arduino sketch`_ is available.

.. _ADK toolkit: https://github.com/palazzem/adk-toolkit
.. _Arduino sketch: https://github.com/palazzem/arduino-udoo-rover

Features
--------

Compatible rover can be controlled using two main components:

* a direction controller which sends serial commands to any ADK compatible devices;
* a ``SeekBar`` to control how much power is sent by motor driver to DC motors.

Damage protection
-----------------

``SeekBar`` component has a lower and upper bound [0, 400]. However, in this application, there aren't any checks to avoid motor damage.
Motor protection is realized by `Arduino sketch`_.

Twitter commands
----------------

Another feature of this app is to control rover motors using Twitter commands. The application search for valid mentions of rover Twitter account,
parse them to a valid JSON and extract whole text. Using ``commandBuilder`` which is available in ``arduino`` package, the application create
a valid Arduino command and send it via AOA protocol.

You can enable and disable this behaviour clicking on ``Enable twitter commands`` button.
Tweet fetch is not enabled when activity starts.

Change log
----------

0.2.2
~~~~~

* Updated ADK Toolkit to 0.2.0

0.2.1
~~~~~

* Updates Gradle plugin
* Includes `ADK Toolkit`_ library

0.2.0
~~~~~

* Removed Twitter endpoint and OAuth2 flow
* Tweets fetch is done using an external `customized <https://github.com/masci/droidcon2014>`_ endpoint which caches mentions using Twitter Streaming API (it's very fast).

0.1.0
~~~~~

* Rover direction controller on main Activity
* Twitter OAuth2 flow implemented to gather Bearer token
* Tweets fetch is done using /search/ API endpoint (it's not so fast)
* Twitter commands are parsed in valid Arduino commands and sent via serial

License
-------

* Application code: FreeBSD (see ``LICENSE`` file)
* UDOO logo: `Copyright 2013 SECO USA Inc`_

.. _Copyright 2013 SECO USA Inc: http://www.udoo.org/
