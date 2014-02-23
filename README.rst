==================
Android UDOO rover
==================

Android app with ADK support to send/receive commands to/from Arduino rover with MC33926 motor shield. Powered by UDOO prototyping board.

Arduino compatibility
---------------------

This Android application uses ``ADK 2012`` to communicate with an enabled ADK Arduino.
Using Android UI you can send serial commands to Arduino so it can pilot a motor driver to change direction and set speed to DC motors.
It uses `ADK toolkit` library to wrap standard ADK features. You can find a compatible Arduino sketch in this ììì

.. ADK toolkit: github.com/palazzem/adk-toolkit

Documentation
-------------

Compatible rover can be controlled using two main components:

* a direction controller which sends serial commands to any ADK compatible devices;
* a ``SeekBar`` to control how much power is sent by motor driver to DC motors.

**NOTE**: the ``SeekBar`` has a lower and upper limit [0, 400]. However in this application there aren't any controls to avoid motor damage.
 All damage protection checks are made by Arduino sketch so you can safely deploy and edit this application without any fear
 to damage your Arduino or motor.

Twitter commands
----------------

Another feature of this app is to control rover motors using Twitter commands. The application search for valid mentions of rover Twitter account,
parse them to a valid JSON and extract whole text. Using ``commandBuilder`` which is available in ``arduino`` package, the application create
a valid Arduino command and send it via AOA protocol.

You can enable and disable this behaviour clicking on ``Enable twitter commands`` button.
Tweet fetch is not enabled when activity starts.

Change log
----------

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
