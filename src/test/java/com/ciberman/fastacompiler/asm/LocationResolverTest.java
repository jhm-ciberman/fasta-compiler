package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.mem.MemLocation;
import com.ciberman.fastacompiler.ir.IntConst;
import com.ciberman.fastacompiler.ir.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocationResolverTest {

    @Test
    void locationShouldResolveLocationInReg() {
        Value value = new IntConst(100);

        LocationResolver locationResolver = new LocationResolver();
        locationResolver.REG_AX.setContent(value);

        Location location = locationResolver.location(value);
        Assertions.assertTrue(location.isReg());
        Assertions.assertEquals("ax", location.getName());
    }

    @Test
    void locationShouldResolveLocationInMem() {
        Value value = new IntConst(100);

        LocationResolver locationResolver = new LocationResolver();
        MemLocation expectedLocation = locationResolver.saveInMem(value);

        Location location = locationResolver.location(value);
        Assertions.assertEquals(location, expectedLocation);
    }

    @Test
    void shouldThrownWhenLocationIsNotPresent() {
        Value value = new IntConst(100);

        LocationResolver locationResolver = new LocationResolver();
        Assertions.assertThrows(RuntimeException.class, () -> {
            locationResolver.location(value);
        });
    }

    @Test
    void locationOrNewShouldCreateANewLocationWhenTheValueIsNotPresent() {
        LocationResolver locationResolver = new LocationResolver();
        Location location = locationResolver.locationOrNew(new IntConst(100));
        Assertions.assertEquals(ConstMemLocation.class, location.getClass());
    }

    @Test
    void memLocationShouldCreateANewLocationWhenTheValueIsNotPresent() {
        LocationResolver locationResolver = new LocationResolver();
        Location location = locationResolver.memLocationOrNew(new IntConst(100));
        Assertions.assertEquals(ConstMemLocation.class, location.getClass());
    }
}