package com.mfava.accountingengine.utils

import ma.glasnost.orika.MapperFactory
import ma.glasnost.orika.impl.ConfigurableMapper
import org.springframework.stereotype.Component

@Component
class ModelMapper : ConfigurableMapper() {

    internal lateinit var mapperFactory: MapperFactory

    override fun configure(factory: MapperFactory) {
        super.configure(factory)
        this.mapperFactory = factory


    }
}
