/**
 * END USER LICENSE AGREEMENT (“EULA”)
 *
 * READ THIS AGREEMENT CAREFULLY (date: 9/13/2011):
 * http://www.akiban.com/licensing/20110913
 *
 * BY INSTALLING OR USING ALL OR ANY PORTION OF THE SOFTWARE, YOU ARE ACCEPTING
 * ALL OF THE TERMS AND CONDITIONS OF THIS AGREEMENT. YOU AGREE THAT THIS
 * AGREEMENT IS ENFORCEABLE LIKE ANY WRITTEN AGREEMENT SIGNED BY YOU.
 *
 * IF YOU HAVE PAID A LICENSE FEE FOR USE OF THE SOFTWARE AND DO NOT AGREE TO
 * THESE TERMS, YOU MAY RETURN THE SOFTWARE FOR A FULL REFUND PROVIDED YOU (A) DO
 * NOT USE THE SOFTWARE AND (B) RETURN THE SOFTWARE WITHIN THIRTY (30) DAYS OF
 * YOUR INITIAL PURCHASE.
 *
 * IF YOU WISH TO USE THE SOFTWARE AS AN EMPLOYEE, CONTRACTOR, OR AGENT OF A
 * CORPORATION, PARTNERSHIP OR SIMILAR ENTITY, THEN YOU MUST BE AUTHORIZED TO SIGN
 * FOR AND BIND THE ENTITY IN ORDER TO ACCEPT THE TERMS OF THIS AGREEMENT. THE
 * LICENSES GRANTED UNDER THIS AGREEMENT ARE EXPRESSLY CONDITIONED UPON ACCEPTANCE
 * BY SUCH AUTHORIZED PERSONNEL.
 *
 * IF YOU HAVE ENTERED INTO A SEPARATE WRITTEN LICENSE AGREEMENT WITH AKIBAN FOR
 * USE OF THE SOFTWARE, THE TERMS AND CONDITIONS OF SUCH OTHER AGREEMENT SHALL
 * PREVAIL OVER ANY CONFLICTING TERMS OR CONDITIONS IN THIS AGREEMENT.
 */

package com.akiban.server.types3.mcompat.mcasts;

import com.akiban.server.types3.TCast;
import static com.akiban.server.types3.mcompat.mcasts.MNumericCastBase.*;
import com.akiban.server.types3.mcompat.mtypes.MApproximateNumber;
import com.akiban.server.types3.mcompat.mtypes.MNumeric;
import com.akiban.server.types3.mcompat.mtypes.MString;
import com.akiban.server.types3.texpressions.Constantness;

public class Cast_From_Unsigned_Mediumint
{     
    /**
     * TODO:
     * 
     * DATE
     * DATETIME
     * TIME
     * TIMESTAMP
     * YEAR
     * 
     * BIT
     * CHAR
     * BINARY
     * VARBINARY
     * TINYBLOG
     * TINYTEXT
     * TEXT
     * MEDIUMBLOB
     * MEDIUMTEXT
     * LONGBLOG
     * LONTTEXT
     * 
     */
    public static final TCast TO_TINYINT = new FromInt64ToInt8(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.TINYINT, false, Constantness.UNKNOWN);
    
    public static final TCast TO_UNSIGNED_TINYINT = new FromInt64ToInt16(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.TINYINT_UNSIGNED, false, Constantness.UNKNOWN);
    
    public static final TCast TO_SMALLINT = new FromInt64ToInt16(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.SMALLINT, false, Constantness.UNKNOWN);
    
    public static final TCast TO_UNSIGNED_SMALLINT = new FromInt64ToInt32(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.SMALLINT_UNSIGNED, false, Constantness.UNKNOWN);
    
    public static final TCast TO_MEDIUMINT = new FromInt64ToInt32(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.MEDIUMINT, false, Constantness.UNKNOWN);
    
    public static final TCast TO_INT = new FromInt64ToInt32(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.INT, false, Constantness.UNKNOWN);
    
    public static final TCast TO_UNSIGNED_INT = new FromInt64ToInt64(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.INT_UNSIGNED, true, Constantness.UNKNOWN);
    
    public static final TCast TO_BIGINT = new FromInt64ToInt64(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.BIGINT, false, Constantness.UNKNOWN);
    
    public static final TCast TO_UNSIGNED_BIGINT = new FromInt64ToInt64(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.BIGINT_UNSIGNED, true, Constantness.UNKNOWN);
    
    public static final TCast TO_DOUBLE = new FromInt64ToDouble(MNumeric.MEDIUMINT_UNSIGNED, MApproximateNumber.DOUBLE, false, Constantness.UNKNOWN);
    
    public static final TCast TO_DECIMAL = new FromInt64ToDecimal(MNumeric.MEDIUMINT_UNSIGNED, MNumeric.DECIMAL, false, Constantness.UNKNOWN);
    
    public static final TCast TO_VARCHAR = new FromInt64ToString(MNumeric.MEDIUMINT_UNSIGNED, MString.VARCHAR, false, Constantness.UNKNOWN);

}