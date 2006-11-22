package com.planet_ink.coffee_mud.Commands;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

import java.util.*;

/* 
   Copyright 2000-2006 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
public class Alias extends StdCommand
{
    private String[] access={getScr("Alias","cmd")};
    public String[] getAccessWords(){return access;}
    public boolean execute(MOB mob, Vector commands)
        throws java.io.IOException
    {
        if((mob.playerStats()==null)||(mob.session()==null))
            return false;
        PlayerStats ps=mob.playerStats();
        while((mob.session()!=null)&&(!mob.session().killFlag()))
        {
            StringBuffer menu=new StringBuffer(getScr("Alias","aliasdefs"));
            String[] aliasNames=ps.getAliasNames();
            for(int i=0;i<aliasNames.length;i++)
                menu.append(CMStrings.padRight((i+1)+". "+aliasNames[i],15)+": "+ps.getAlias(aliasNames[i])+"\n\r");
            menu.append(getScr("Alias","new",""+(aliasNames.length+1)));
            mob.tell(menu.toString());
            String which=mob.session().prompt(getScr("Alias","enterselec"),"");
            if(which.length()==0)
                break;
            int num=CMath.s_int(which);
            String selection=null;
            if((num>0)&&(num<=(aliasNames.length)))
            {
                selection=aliasNames[num-1];
                if(mob.session().choose(getScr("Alias","delmodalias",selection),getScr("Alias","delmodopt"),getScr("Alias","delmoddef")).equals(getScr("Alias","delmoddef2")))
                {
                    ps.delAliasName(selection);
                    mob.tell(getScr("Alias","deleted"));
                    selection=null;
                }
            }
            else
            if(num<=0)
                break;
            else
            {
               selection=mob.session().prompt(getScr("Alias","newalias"),"").trim().toUpperCase();
               if(selection.length()==0)
                   selection=null;
               else
               if(ps.getAlias(selection).length()>0)
               {
                   selection=null;
                   mob.tell(getScr("Alias","taken"));
               }
               else
               {
                   for(int i=0;i<selection.length();i++)
                       if(!Character.isLetterOrDigit(selection.charAt(i)))
                       {
                           selection=null;
                           break;
                       }
                   if(selection==null)
                       mob.tell(getScr("Alias","letornum"));
                   else
                       ps.addAliasName(selection);
               }
            }
            if(selection!=null)
            {
                mob.session().rawPrintln(getScr("Alias","aliasval",selection));
                String value=mob.session().prompt(": ","").trim();
                value=CMStrings.replaceAll(value,"<","");
                value=CMStrings.replaceAll(value,"&","");
                if((value.length()==0)&&(ps.getAlias(selection).length()>0))
                    mob.tell(getScr("Alias","nochange"));
                else
                if(value.length()==0)
                {
                    mob.tell(getScr("Alias","aborted"));
                    ps.delAliasName(selection);
                }
                else
                {
                    ps.setAlias(selection,value);
                    mob.tell(getScr("Alias","changed"));
                }
            }
        }
        return true;
    }
    
    public boolean canBeOrdered(){return true;}

    
}

