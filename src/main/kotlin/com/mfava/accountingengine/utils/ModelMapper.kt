package com.mfava.accountingengine.utils

import lombok.extern.slf4j.Slf4j
import ma.glasnost.orika.MapperFactory
import ma.glasnost.orika.impl.ConfigurableMapper
import org.springframework.stereotype.Component

@Component
@Slf4j
class ModelMapper : ConfigurableMapper() {

    internal lateinit var mapperFactory: MapperFactory

    override fun configure(factory: MapperFactory) {
        super.configure(factory)
        this.mapperFactory = factory




    }
}
