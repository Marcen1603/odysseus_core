PAGE EPIS_Termine

$EPIS_Termine:  * <font[ ]class="header">Termine</font><br>*\n
                ([\t ])*<br>*\n
                \t\n
                \t\n
                ($termin)+;

$termin:        <table[ ]border="0"[ ]cellpadding="0"[ ]cellspacing="0"[ ]width="400">*\n*\n
                $dateinfo</table>*\n
                <table>$vortragsinfo</table>*\n*\n;

$dateinfo:      *<FONT*>$a</font>
                {
                    System.out.println($a);                    
                }
                *<IMG*/tr>*\n;

$vortragsinfo:  *
                (\t<b>$b[:]</b><br>*\n
                *["]$c["]*<br><br>*\n*\n*\n)+
                *</tr>*\n
                {
                    System.out.println($b);                    
                    System.out.println($c);                    
                };

END
