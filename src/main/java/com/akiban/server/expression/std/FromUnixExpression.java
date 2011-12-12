/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.akiban.server.expression.std;

import com.akiban.server.error.WrongExpressionArityException;
import com.akiban.server.expression.Expression;
import com.akiban.server.expression.ExpressionComposer;
import com.akiban.server.expression.ExpressionEvaluation;
import com.akiban.server.expression.ExpressionType;
import com.akiban.server.service.functions.Scalar;
import com.akiban.server.types.AkType;
import com.akiban.server.types.NullValueSource;
import com.akiban.server.types.ValueSource;
import com.akiban.server.types.util.ValueHolder;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

public class FromUnixExpression extends AbstractCompositeExpression
{
    @Scalar("from_unixtime")
    public static final ExpressionComposer COMPOSER = new ExpressionComposer ()
    {
        @Override
        public Expression compose(List<? extends Expression> args)
        {
            return new FromUnixExpression(args);
        }

        @Override
        public void argumentTypes(List<AkType> argumentTypes)
        {
            int s = argumentTypes.size();
            switch(s)
            {
                case 2: argumentTypes.set(1, AkType.VARCHAR); // fall thru
                case 1: argumentTypes.set(0, AkType.LONG); break;
                default: throw new WrongExpressionArityException(1, s);
            }
        }

        @Override
        public ExpressionType composeType(List<? extends ExpressionType> argumentTypes)
        {
            int s = argumentTypes.size();
            switch(s)
            {
                case 2: return ExpressionTypes.varchar(argumentTypes.get(1).getScale() * 4); // each specifier is to be replaced with a "number", max length is 4
                case 1: return ExpressionTypes.DATETIME;
                default: throw new WrongExpressionArityException(1, s);
            }
        }
    };

    @Override
    protected boolean nullIsContaminating() 
    {
        return true;
    }

    @Override
    protected void describe(StringBuilder sb) 
    {
        sb.append("FROM_UNIXTIME()");
    }
    
    private static class InnerEvaluation extends AbstractCompositeExpressionEvaluation
    {
        public InnerEvaluation (List< ? extends ExpressionEvaluation> ev)
        {
            super(ev);
        }

        @Override
        public ValueSource eval() 
        {
            ValueSource dateS = children().get(0).eval();
            if (dateS.isNull()) return NullValueSource.only();
            long unix = dateS.getLong() * 1000;
         
            switch(children().size())
            {
                case 1:     return new ValueHolder(AkType.DATETIME, new DateTime(unix));
                
                default:    ValueSource str = children().get(1).eval();
                            if (str.isNull()) 
                                return NullValueSource.only();
                            else 
                                return new ValueHolder(AkType.VARCHAR, getFormatted(new MutableDateTime(unix), str.getString()));
            }
            
        }
        
        
        private static String getFormatted (MutableDateTime date, String format)
        {
            String[] frmList = format.split("\\%");
            StringBuilder buffer = new StringBuilder(frmList[0]);
          
            for (int n = 1; n < frmList.length; ++n)
                if (frmList[n].length() == 0)
                {
                    buffer.append("%");
                    ++n;
                }
                else
                    try
                    { 
                        String s = frmList[n].charAt(0) + "";
                        buffer.append(frmList[n].replaceFirst(s, DateTimeField.valueOf(s).get(date)));
                    }
                    catch (IllegalArgumentException ex) // unknown specifiers are treated as regular chars
                    {
                        buffer.append(frmList[n]);
                    }
            
            return buffer.toString();
        }
    }
    
    /**
     * Takes an UNIX_TIME, which is the number of SECONDS from epoch and optionally 
     * a format str
     * 
     * @param ex 
     * @return  DATETIME in <b>current</b> timezone if no format string is passed
     *          VARCHAR representing the datetime formated accordingly if format string is passed
     */
    public FromUnixExpression (List<? extends Expression> ex)
    {
        super(checkArg(ex), ex);
    }

    protected static AkType checkArg (List<? extends Expression> ex)
    {
        if (ex.size() != 2 & ex.size() != 1) throw new WrongExpressionArityException(1, ex.size());
        return ex.size() == 2 ? AkType.VARCHAR : AkType.DATETIME;
    }

    @Override
    public ExpressionEvaluation evaluation() 
    {
        return new InnerEvaluation(childrenEvaluations());
    }
}