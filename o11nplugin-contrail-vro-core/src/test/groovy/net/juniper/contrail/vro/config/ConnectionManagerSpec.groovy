/*
 * Copyright (c) 2018 Juniper Networks, Inc. All rights reserved.
 */

package net.juniper.contrail.vro.config

import net.juniper.contrail.api.ApiConnectorMock
import net.juniper.contrail.vro.model.Connection
import net.juniper.contrail.vro.model.ConnectionInfo
import spock.lang.Specification

class ConnectionManagerSpec extends Specification {

    def info = new ConnectionInfo("host", 8080, "user", "secret")
    def connector = new ApiConnectorMock(info.hostname, info.port)
    def connection = new Connection(info, new ApiConnectorMock(info.hostname, info.port))
    def repository = Mock(ConnectionRepository)
    def factory = Mock(ConnectorFactory)
    def manager = new ConnectionManager(repository, factory)

    def "Calling create inserts connection into repository" () {
        given:
        factory.create(_) >> connector

        when:
        manager.create(info.hostname, info.port, info.username, info.password)

        then:
        1 * repository.addConnection(_)
    }

    def "Calling deleted removes connection from repository" () {
        when:
        manager.delete(connection)

        then:
        1 * repository.removeConnection(connection)
    }
}