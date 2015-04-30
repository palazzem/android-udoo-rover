==========
DroidRover
==========

Android embedded app that controls an Arduino rover powered by MC33926 motor shield using the `ADK2012`_.
The first prototype has been developed using the `UDOO Quad`_ prototyping board.

.. _ADK2012: http://developer.android.com/tools/adk/adk2.html
.. _UDOO Quad: http://www.udoo.org/udoo-dual-quad/

External dependencies
---------------------

* `ADK toolkit`_ (Gradle/Maven Android library)
* External `Django service`_ that simplifies Twitter access
* `Arduino sketch`_ for rover control

.. _ADK toolkit: https://github.com/palazzem/adk-toolkit
.. _Django service: https://github.com/masci/droidcon2014
.. _Arduino sketch: https://github.com/palazzem/arduino-udoo-rover

Features
--------

Compatible rovers can be controlled using the following approaches:

* GUI manual mode: through a directional PAD and a ``SeekBar`` that controls DC motors power
* Myo manual mode: the rover could be controlled using a `Myo armband`_
* Twitter mode: reading the `@DroidRover`_ mentions, that are turned in Arduino commands

.. _Myo armband: https://www.thalmic.com/en/myo/
.. _@DroidRover: https://twitter.com/droidrover

Damage protection
-----------------

``SeekBar`` lower and upper bounds [0, 400] are not meant to be used as a "damage protection system".
The Arduino sketch realizes the `motor protection`_ according to MC33926 motor shield specs.

.. _motor protection: https://github.com/palazzem/arduino-udoo-rover/blob/master/rover/rover.ino#L141

Twitter commands
----------------

The application searches and parses DroidRover's mentions into a valid JSON extracting the whole text.
Using the ``commandBuilder`` method available in the ``arduino`` package, the app creates a valid
Arduino command that is sent via the AOA protocol.

You can enable and disable this behavior clicking the ``Enable twitter commands`` button.
Tweets fetching isn't enabled by default.

Myo Armband
-----------

The options menu provides the ``Myo pairing`` action that opens the built-in ``ScanActivity`` to
connect the armband. After the connection is established, use the traditional gestures to control
the rover (``WAVE_IN``, ``WAVE_OUT``, ``FIST``, ``FINGERS_SPREAD``).

Change log
----------

0.3.0 [2015-05-01]
~~~~~~~~~~~~~~~~~~

* Project skeleton updated for Gradle 2.2.1 and Android Studio >=1.0.0
* ADK Toolkit updated to version 0.3.0
* Created the ``Rover`` class to manage the controlled robot
* Code style refactoring
* Added Myo control

0.2.2 [2014-03-24]
~~~~~~~~~~~~~~~~~~

* Updated the ADK Toolkit to 0.2.0

0.2.1 [2014-03-10]
~~~~~~~~~~~~~~~~~~

* Updates Gradle plugin
* Includes the `ADK Toolkit`_ library

0.2.0 [2014-02-05]
~~~~~~~~~~~~~~~~~~

* Removed Twitter endpoint and OAuth2 flow
* Tweets fetch is done using an external customized endpoint which caches mentions using Twitter Streaming API

0.1.0 [2014-02-02]
~~~~~~~~~~~~~~~~~~

* Rover direction controller on main Activity
* Twitter OAuth2 flow implemented to gather the Bearer token
* Tweets fetching is done using the ``/search/`` endpoint
* Twitter commands are trasformed into valid Arduino commands and then sent through the serial port

License
-------

* Application code: FreeBSD (see ``LICENSE`` file)
* UDOO logo: `Copyright 2013 SECO USA Inc`_

.. _Copyright 2013 SECO USA Inc: http://www.udoo.org/
