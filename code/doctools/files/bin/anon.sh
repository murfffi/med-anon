#!/bin/sh
java -cp "../conf/:../*:../lib/*:../gate71/bin/*:../gate71/lib/*" -Dgate.home=../gate71 -Dapp.loc=../conf/anon_v1.gapp medanon.Main $*