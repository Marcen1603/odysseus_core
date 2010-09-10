CREATE TABLE `auction` (
  `id` int(11) NOT NULL,
  `itemname` varchar(50) NOT NULL,
  `description` varchar(50) NOT NULL,
  `initialbid` int(11) NOT NULL,
  `reserve` int(11) NOT NULL,
  `expires` bigint(20) NOT NULL,
  `seller` int(11) NOT NULL,
  `category` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) 


CREATE TABLE  `bid` (
  `timestamp` bigint(20) NOT NULL,
  `auction` int(11) NOT NULL,
  `bidder` int(11) NOT NULL,
  `datetime` bigint(20) NOT NULL,
  `price` double NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) 


CREATE TABLE  `category` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(50) NOT NULL,
  `parentId` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) 


CREATE TABLE  `auction`.`person` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `creditcard` varchar(50) NOT NULL,
  `city` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  PRIMARY KEY  (`id`)
)