/**
 * Copyright (c) 2002-2011 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.impl.util;

import static org.junit.Assert.assertEquals;
import static org.neo4j.kernel.impl.util.Bits.bits;

import org.junit.Test;

public class TestBits
{
    @Test
    public void pushLeft() throws Exception
    {
        Bits bits = bits( 16 );
        bits.pushLeft( (byte) 3, 3 );
        bits.pushLeft( (short) 100, 7 );
        bits.pushLeft( (int) -1 );
        
        assertEquals( -1, bits.pullRightInt() );
        assertEquals( (short) 100, bits.pullRightShort( 7 ) );
        assertEquals( (byte) 3, bits.pullRightByte( 3 ) );
    }
    
    @Test
    public void pushRight()
    {
        Bits bits = bits( 16 );
        bits.pushRight( (byte) 3, 3 );
        bits.pushRight( (short) 100, 7 );
        bits.pushRight( (int) -1 );
        
        assertEquals( -1, bits.pullLeftInt() );
        assertEquals( (short) 100, bits.pullLeftShort( 7 ) );
        assertEquals( (byte) 3, bits.pullLeftByte( 3 ) );
    }
    
    @Test
    public void asBytes() throws Exception
    {
        int numberOfBytes = 14;
        Bits bits = bits( numberOfBytes );
        for ( byte i = 0; i < numberOfBytes; i++ )
        {
            bits.pushRight( i );
        }
        
        // They come out in the revers order of which they got pushed in, LIFO style
        byte[] bytes = bits.asLeftBytes();
        for ( byte i = 0; i < numberOfBytes; i++ )
        {
            assertEquals( numberOfBytes-1-i, bytes[i] );
        }
        
        bytes = Bits.bitsFromBytesLeft( bytes ).asLeftBytes();
        for ( byte i = 0; i < numberOfBytes; i++ )
        {
            assertEquals( numberOfBytes-1-i, bytes[i] );
        }
    }
    
    @Test
    public void doubleAsBytes() throws Exception
    {
        double[] array1 = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 };
        Bits bits = Bits.bits( array1.length*8 );
        for ( double value : array1 )
        {
            bits.pushRight( Double.doubleToRawLongBits( value ) );
        }
        String first = bits.toString();
        byte[] asBytes = bits.asLeftBytes();
        String other = Bits.bitsFromBytesLeft( asBytes ).toString();
        assertEquals( first, other );
    }
}
