INSERT INTO blueprints(author, name) VALUES ('john','house') ON CONFLICT DO NOTHING;
INSERT INTO blueprint_points(author,name,idx,x,y) VALUES ('john','house',0,0,0) ON CONFLICT DO NOTHING;
INSERT INTO blueprint_points(author,name,idx,x,y) VALUES ('john','house',1,10,0) ON CONFLICT DO NOTHING;
INSERT INTO blueprint_points(author,name,idx,x,y) VALUES ('john','house',2,10,10) ON CONFLICT DO NOTHING;
INSERT INTO blueprint_points(author,name,idx,x,y) VALUES ('john','house',3,0,10) ON CONFLICT DO NOTHING;
