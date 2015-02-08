mosaik-zmq simulator for use in mosaik (http://mosaik.offis.de/)

After setting up mosaik install mosaik-zmq with following command:

pip install *path*/mosaik-zmq-0.1.tar.gz


To add the simulator to a scenario add this to sim_config:

sim_config = {
    'ZMQ': {
        'cmd': 'mosaik-zmq %(addr)s',
    },
}

And the following code in the scenario setup:

odysseusModel = world.start('ZMQ', step_size=60, duration=END)
ody = odysseusModel.Socket(host='tcp://*:', port=5558, socket_type='PUB')


To connect in Odysseus the MosaikAccessAO can be used.
